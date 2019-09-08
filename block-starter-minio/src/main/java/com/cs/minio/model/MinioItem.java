package com.cs.minio.model;

import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.Data;

import java.util.Date;

/**
 * MinioItem
 *
 * @blame csz
 */
@Data
public class MinioItem {

	private String objectName;
	private Date lastModified;
	private String etag;
	private Long size;
	private String storageClass;
	private Owner owner;
	private boolean isDir;
	private String category;

	public MinioItem(Item item) {
		this.objectName = item.objectName();
		this.lastModified = item.lastModified();
		this.etag = item.etag();
		this.size = (long) item.size();
		this.storageClass = item.storageClass();
		this.owner = item.owner();
		this.isDir = item.isDir();
		this.category = isDir ? "dir" : "file";
	}

}
