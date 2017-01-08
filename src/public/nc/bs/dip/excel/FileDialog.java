package nc.bs.dip.excel;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class FileDialog {
	
	private JFileChooser fileChooser;
	private Component UIParent;
	private File defaultPath = null;
	
	public FileDialog(Component parent){
		UIParent = parent;
	}

	public File openFile(){
		if(JFileChooser.APPROVE_OPTION == getDlg().showOpenDialog(UIParent)){
			defaultPath = getDlg().getCurrentDirectory();
			return getDlg().getSelectedFile();
		}
		return null;
	}
	
	public File saveFile(){
		if(JFileChooser.APPROVE_OPTION == getDlg().showSaveDialog(UIParent)){
			defaultPath = getDlg().getCurrentDirectory();
			return getDlg().getSelectedFile();
		}
		return null;
	}
	
	public JFileChooser getDlg(){
		if(fileChooser == null){
			fileChooser = new JFileChooser();
			if(defaultPath != null){
				fileChooser.setCurrentDirectory(defaultPath);
			}
			fileChooser.setFileFilter(new UIFileFilter());
		}
		return fileChooser;
	}
}
