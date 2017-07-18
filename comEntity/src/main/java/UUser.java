import java.util.Date;
import lombok.Data;

@Data
public class UUser implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	private Integer id;
	private String name;
	private String loginName;
	private String roleName;
	private Date createTime;
	private Date lastLoginTime;
	private Integer dataFlag;
	private String password;

}