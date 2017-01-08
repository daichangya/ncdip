package nc.bs.dip.excel;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class UIFileFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		if(f.isFile() && f.getName().endsWith(".xls")){
			return true;
		}else if(f.isDirectory()){
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "EXCELÎÄ¼þ£¨*.xls£©";
	}

}
