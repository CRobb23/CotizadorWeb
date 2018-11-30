package models;
import play.*;

import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;

import java.util.*;

@Entity
public class ER_Store extends Model {
	
	@Required
	@ManyToOne
	public ER_Distributor distributor;
	
	@ManyToMany (cascade=CascadeType.PERSIST)
	@JoinTable(name="ER_Store_ER_Administrator",
			joinColumns={@JoinColumn(name="ER_Store_id",nullable=false)},
			inverseJoinColumns={@JoinColumn(name="administrators_id",nullable=false)},
			uniqueConstraints = {@UniqueConstraint(columnNames = {"ER_Store_id","administrators_id"})})
		public List<ER_User> administrators;
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;

	@Required
	public Boolean active;


	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="ER_Store_ER_User",
			joinColumns={@JoinColumn(name="ER_Store_id",nullable=false)},
			inverseJoinColumns={@JoinColumn(name="sellers_id",nullable=false)},
			uniqueConstraints = {@UniqueConstraint(columnNames = {"ER_Store_id","sellers_id"})})
	public List<ER_User> sellers;


}
