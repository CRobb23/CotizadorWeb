package models;

import javax.persistence.*;

import play.db.jpa.Model;

import java.util.Date;

@Entity
public class ER_Incident_Comments extends Model{

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User user;

    @Column(length=8000)
    public String comment;

    public Date reviewDate;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_Incident incident;

    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    @JoinColumn(name="status_id", nullable=true)
    public ER_ReviewStatus status;

}
