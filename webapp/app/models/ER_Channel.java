package models;

import java.util.List;

import javax.persistence.*;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Channel extends Model {
    
	@Required
	@MaxSize(150)
	@Column(length=150)
	public String name;

	@Required
	public Boolean active;
	
	@Column(name="is_public")
	public Boolean isPublic;
	
	@Column(name="transfer_code")
	public String transferCode;

	@ManyToOne
	public ER_Portfolio_Type portfolioType;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name="ER_Channel_Administrator",
			joinColumns={@JoinColumn(name="channel_id",nullable=false)},
			inverseJoinColumns={@JoinColumn(name="administrator_id",nullable=false)},
			uniqueConstraints = {@UniqueConstraint(columnNames = {"channel_id","administrator_id"})})
	public List<ER_User> administrators;
}
