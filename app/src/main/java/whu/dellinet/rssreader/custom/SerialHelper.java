package whu.dellinet.rssreader.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import whu.dellinet.rssreader.entity.ItemListEntity;

public class SerialHelper {
	private static SerialHelper helper;
	
	private SerialHelper() {
		// TODO Auto-generated constructor stub
	};
	
	public static SerialHelper getInstance() {
		if (helper==null) {
			helper=new SerialHelper();
		}
		return helper;
	}
	
	public void saveObject(Serializable serializable,File file) {
		FileOutputStream fos=null;
		ObjectOutputStream oos=null;
		try {
			fos=new FileOutputStream(file);
			oos=new ObjectOutputStream(fos);
			oos.writeObject(serializable);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if (oos!=null) {
				try {
					oos.close();
					oos=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fos!=null) {
				try {
					fos.close();
					fos=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public Serializable readObject(File file) {
		if (!file.exists()) {
			return null;
		}
		FileInputStream fis=null;
		ObjectInputStream ois=null;
		try {
			fis=new FileInputStream(file);
			ois=new ObjectInputStream(fis);
			ItemListEntity iListEntity=(ItemListEntity) ois.readObject();
			return iListEntity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}finally{
			if (ois!=null) {
				try {
					ois.close();
					ois=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fis!=null) {
				try {
					fis.close();
					fis=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
