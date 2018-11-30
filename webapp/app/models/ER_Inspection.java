package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import net.sf.oval.constraint.MaxLength;
import play.data.binding.As;
import play.db.jpa.Model;

@Entity
public class ER_Inspection extends Model {
	
	@MaxLength(200)
	@Column(length=200)
	public String address;
	
	@ManyToOne
	public ER_Inspection_Type type;

    @Column(name = "Insurable")
    public String insurable;

	@Column(name = "commonData")
	public String commonData;
	
	@MaxLength(50)
	@Column(length=50)
	public String inspectionNumber;
	
	public boolean inspected;
	
	@OneToOne(mappedBy="inspection")
	public ER_Incident incident;
	
	public Date creationDate;
	
	@As("dd/MM/yyyy HH:mm")
	public Date appointmentDate;
	
	public Date inspectionDate;
	
	@Column(name="existent_damage")
	public String existentDamage;

	@Column(name = "soundEquipment")
    public String soundEquipment;

	public ER_Inspection() {
		this.creationDate = new Date();
	}
}
