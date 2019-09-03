package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import ext.GsonExclude;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Distributor extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	@ManyToOne
	public ER_Channel channel;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name="ER_Distributor_Administrator",
			joinColumns={@JoinColumn(name="distributor_id",nullable=false)},
			inverseJoinColumns={@JoinColumn(name="administrator_id",nullable=false)},
			uniqueConstraints = {@UniqueConstraint(columnNames = {"distributor_id","administrator_id"})})
	public List<ER_User> administrators;

	@Required
	public Boolean active;

	@Required
	@Email
	@MaxSize(75)
	@Column(length=75)
	public String inspectionEmail;

	@GsonExclude
	@OneToOne(mappedBy="distributor")
	public ER_Distributor_Custom_Logo logo;

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String,Object>();
		if (id!=null) {
			map.put("id", this.id);
		}
		if (name!=null) {
			map.put("name", this.name);
		}
		if (active) {
			map.put("active", active);
		}
		return map;
	}

}
