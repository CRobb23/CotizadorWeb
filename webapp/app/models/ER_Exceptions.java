package models;

import javax.persistence.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import java.util.Date;

@Entity
public class ER_Exceptions extends Model{

    @Required
    @MaxSize(300)
    @Column(length=300)
    public String description;

    public Date exceptionDate;

    //0 = deleted
    //1 = active
    @Required
    public int active;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_Quotation quotation;
}
