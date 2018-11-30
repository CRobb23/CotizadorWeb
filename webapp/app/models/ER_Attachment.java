package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class ER_Attachment extends Model {
    
	public String fileName;
	
	public Integer code;
	
	public Blob attachment;
}
