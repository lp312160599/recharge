package com.dayee.utils;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作类
 * @author 李鹏
 * @version 1.0
 *
 */
public class FileOperation {

	/**
	 * 根据正则表达式查找当前目录下（不包含子目录）与之匹配的文件
	 * @param filePath
	 * 文件路径
	 * @param replacement
	 * 正则表达式
	 * @param isReg
	 * 是否使用正则
	 * @return
	 * 返回找到的内容集合
	 */
	public List<File> findFileBycurrentDirectory(String filePath, String fileName,boolean isReg) {
		File file = new File(filePath);
		List<File> sheetFile = new ArrayList<File>();
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				if(isReg){
				if (f.getName().matches(fileName)) { 
					sheetFile.add(f);
				 }
				}else{
					if (f.getName().equals(fileName)) { 
						sheetFile.add(f);
					 }
				}
			}
		}
		return sheetFile;
	}
	
	/**
	 * 根据正则表达式查找当前目录下（包含子目录）与之匹配的文件
	 * @param folderPath
	 * 文件夹位置
	 * @param replacement
	 * 正则表达式
	 * @param findFiles
	 * 用于装载查找到的File 
	 * @param isReg
	 * 是否使用正则
	 * @return
	 */
	
	public List<File> findFile(String folderPath, String fileName,boolean isReg,List<File> findFiles){
		
		File file = new File(folderPath);
		if(file.isDirectory()){
			for (File file2 : file.listFiles()) {
				if(isReg){
				if(file2.getName().matches(fileName))findFiles.add(file2);
				else  findFile(file2.getPath(), fileName,isReg,findFiles);
				}else{
					if(file2.getName().equals(fileName))findFiles.add(file2);
					else  findFile(file2.getPath(), fileName,isReg,findFiles);
				}
			}
		}
		
		return findFiles;
	}
	
	public File findFile(String folderPath, String fileName,boolean isReg){
		List<File> findFiles = findFile(folderPath, fileName, isReg,new ArrayList<File>());
		if(findFiles.size()!=0)return findFiles.get(0);
		else return null;
	}
 
	/**
	 * 读取文件
	 * @param Path
	 * 文件路径
	 * @return
	 * 返回读取后的内容
	 * @throws IOException
	 */
	public String readFile(String Path) throws IOException{
		File file = new File(Path);
		return readFile(new FileInputStream(file));
	}
	
	public String readFile(InputStream input) throws IOException{
		BufferedReader read = new BufferedReader(new InputStreamReader(input,"UTF-8"));
		StringBuffer sbu = new StringBuffer();
		String temp = null;
		while((temp=read.readLine())!=null)sbu.append(temp).append("\n");
		return sbu.toString();
	}
	
	/**
	 * 保存文件
	 * @param filePath
	 * 保存文件的路径
	 * @param text
	 * 保存文件的内容
	 * @throws FileNotFoundException
	 * 文件没有查找到
	 * @throws UnsupportedEncodingException 
	 */
	public void saveFile(String filePath,String text) throws FileNotFoundException, UnsupportedEncodingException{
		saveFile(filePath, text,null);
	}
	
	public void saveFile(String filePath,String text,String csn) throws FileNotFoundException, UnsupportedEncodingException{
		File file = new File(filePath);
		PrintWriter out = csn==null?new PrintWriter(file):new PrintWriter(file,csn);
		out.print(text);
		out.flush();
		out.close();
	}
	
	/**
	 * 删除文件
	 * @param path
	 */
	public void remove(String path){
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 判断文件路径判断文件是否存在，如果不存在则建立文件包括文件所在的父级目录如不存在同样建立，反之 则不建立
	 * @param path
	 * 文件路径
	 */
	public void exists(String path){
		File file = new File(path);
		exists(file);
	}
	
	/**
	 * 判断文件路径判断文件是否存在，如果不存在则建立文件包括文件所在的父级目录如不存在同样建立，反之 则不建立
	 * @param file
	 * 文件
	 */
	public void exists(File file){
		if(!file.exists()){
				file.mkdirs();
		}
	}
}
