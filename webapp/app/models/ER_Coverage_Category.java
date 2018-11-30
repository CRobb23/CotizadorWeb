package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import ext.GsonExclude;
import ext.NoExposeExclusionStrategy.NoExpose;
import play.data.validation.MaxSize;
import play.db.jpa.Model;

@Entity
public class ER_Coverage_Category extends Model {
	
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@MaxSize(150)
	@Column(length=150)
	public String description;
	
	public Integer code;
	
	@OneToMany(mappedBy="category")
	@GsonExclude
	public List<ER_Coverage> coverages;
}
