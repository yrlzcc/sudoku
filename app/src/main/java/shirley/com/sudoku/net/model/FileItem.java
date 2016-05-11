package shirley.com.sudoku.net.model;

/**
 * 用作文件上传时候的文件
 * @author lephone.zh
 *
 */
public class FileItem {
	
	public static final String MIME_IMG = "image/jpg";
	public static final String MIME_AMR = "audio/amr";
	public static final String MIME_ZIP = "application/zip";
	
	
	
	private String name;
	private String path;
	private String contentType;
	/**
	 * 构造方法
	 * @param path 文件所处的绝对路径  name属性从path中取
	 * @param contentType 文件的类型 图片文件image/jpg  zip文件application/zip 录音文件audio/amr
	 */
	public FileItem(String path, String contentType) {
		super();
		this.name = path.substring(path.lastIndexOf("/")+1);
		this.path = path;
		this.contentType = contentType;
	}
	
	
	/**
	 * 构造方法
	 * @param name 文件名的名字  要带后缀名  如 xx.jpg
	 * @param path 文件所处的绝对路径
	 * @param contentType 文件的类型 图片文件image/jpg  zip文件application/zip 录音文件audio/amr
	 */
	public FileItem(String name, String path, String contentType) {
		super();
		this.name = name;
		this.path = path;
		this.contentType = contentType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public String getPath() {
		return path;
	}
	public String getContentType() {
		return contentType;
	}
	
	
}
