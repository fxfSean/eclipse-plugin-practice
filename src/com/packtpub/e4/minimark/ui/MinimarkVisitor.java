package com.packtpub.e4.minimark.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

public class MinimarkVisitor implements IResourceProxyVisitor, IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceProxy proxy) throws CoreException {
		String name = proxy.getName();
		if (name != null && name.endsWith(".minimark")) {
			processResource(proxy.requestResource());
		}
		return true;
	}
	
	private void processResource(IResource resource) throws CoreException {
		if (resource instanceof IFile && resource.exists()) {
			try {
				IFile file = (IFile)resource;
				InputStream in = file.getContents();
				String htmlName = file.getName().replace(".minimark", ".html");
				IContainer container = file.getParent();
				IFile htmlFile = container.getFile(new Path(htmlName));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				MinimarkTranslator.convert(new InputStreamReader(in),
//						new OutputStreamWriter(System.out)
						new OutputStreamWriter(baos)
						);
				if (baos.size() < 100) {
					System.out.println("Minimark is empty");
					IMarker marker = resource.createMarker(IMarker.PROBLEM);
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					marker.setAttribute(IMarker.MESSAGE, "mini file is empty");
					marker.setAttribute(IMarker.LINE_NUMBER, 1);
					marker.setAttribute(IMarker.CHAR_START, 0);
					marker.setAttribute(IMarker.CHAR_END, 0);
					
				}
				ByteArrayInputStream contents = new ByteArrayInputStream(baos.toByteArray());
				if (htmlFile.exists()) {
					htmlFile.setContents(contents, true, false, null);
				} else {
					htmlFile.create(contents, true, null);
				}
				htmlFile.setDerived(true);
				
			} catch (Exception e) {
				throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, "filed to generate resource"));
			}
			
		}
		
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		boolean deleted = (IResourceDelta.REMOVED & delta.getKind()) != 0;
		
		IResource resource = delta.getResource();
		String name = resource.getName();
		if (name.endsWith(".minimark")) {
			if (deleted) {
				String htmlName = name.replace(".minimark", ".html");
				IFile htmlFile = resource.getParent().getFile(new Path(htmlName));
				if (htmlFile.exists()) {
					htmlFile.delete(true, null);
				}
			} else {
				processResource(resource);
			}	
		} else if (name.endsWith(".html")) {
			String minimarkName = name.replace(".html", ".minimark");
			IFile minimarkFile = resource.getParent().getFile(new Path(minimarkName));
			if (minimarkFile.exists()) {
				processResource(minimarkFile);
			}
		}
		
//		if (resource.getName().endsWith(".minimark")) {
//			processResource(resource);
//		}
		return true;
	}

}
