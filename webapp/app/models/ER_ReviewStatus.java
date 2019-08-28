package models;

import play.data.validation.MaxSize;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ER_ReviewStatus extends Model {

    @MaxSize(50)
    @Column(length=50)
    public String name;

    public boolean forQAUser;
    public boolean forCommercialQAUser;
}
