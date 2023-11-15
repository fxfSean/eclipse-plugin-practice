package com.packtpub.e4.minimark.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddMinimarkHandler  extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof IStructuredSelection) {
			Iterator<?> iterator = ((IStructuredSelection)sel).iterator();
			while (iterator.hasNext()) {
				Object object = (Object) iterator.next();
				if (object instanceof IProject) {
					try {
						addProjectNature((IProject)object, MinimarkNature.ID);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			}
			
		}
		return null;
	}

	private void addProjectNature(IProject object, String nature) throws CoreException {
		IProjectDescription description = object.getDescription();
		ArrayList<String> natures = new ArrayList<String>(Arrays.asList(description.getNatureIds()));
		if (!natures.contains(nature)) {
			natures.add(nature);
			description.setNatureIds(natures.toArray(new String[0]));
			object.setDescription(description, null);
		}
		
	}
	
	

}
