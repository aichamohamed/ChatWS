package rt3.group;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.text.html.HTMLDocument.Iterator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/chat")
@Produces("application/json")

public class ChatManager {
	static int userMapId = 0;
	static int msgMapId = 0;
	 
	 // if the fields are private Map return null on "id" and "val"
   //	private static Map<Integer, User > users = new HashMap<>();
  //	private static Map<Integer,  Message> messages = new HashMap<>();
	
//users
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Integer, User> getUsers(){
		return StoreData.allUsers;
	}
	
	@PUT
	@Path("/adduser")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertUser(@QueryParam("login") String login) {
		
		StoreData.allUsers.put(++userMapId, new User(userMapId, login));
	}

//	public void insertUser(User user) {users.put(user.id, new User(user.id, user.login));}

//messages
	  @GET
	  @Path("allmessages")
	  @Produces(MediaType.APPLICATION_JSON)
	  public Map<Integer,  Message> getMessages(){
		  return StoreData.allMessages ;
	  }
	  
	 //list of inbox of a given user
	  @GET
	  @Path("usermessages")
	  @Produces(MediaType.APPLICATION_JSON)
	  public Map<Integer, Message> geMessages(@QueryParam("userId") Integer userId){
		 
		  Map<Integer, Message> inbox = new HashMap<>();
		 
		  for(Entry<Integer, Message> msgObj : StoreData.allMessages.entrySet()) {
			  if (msgObj.getValue().getIdRecepteur() == userId) {
				  inbox.put(msgObj.getKey(), msgObj.getValue());
				}
		  }
		  
		  return inbox;
	  }	   

	  @PUT
	  @Path("send")
	  public void sendMessage(@QueryParam("idE") Integer idE,
			  @QueryParam("idR") Integer idR, @QueryParam("msg") String msg) {
		  if(User.userExist(idE) && User.userExist(idR)) {
			  StoreData.allMessages.put(++msgMapId, new Message(msgMapId,idE, idR, msg));
		  }
		  else {
			  System.out.println("Erreur! : Les deux correspondants doivent être "
							+ "ajouté à la Map d'abord");
		  }
	  }
	 
}

class User{
	Integer id;
	String login;
	
	public User(Integer id, String login) {
		this.id=id;
		this.login = login;
	}

	//check wether a user is created or not
	public static boolean userExist(int userId) {
		for(Entry<Integer, User> user : StoreData.allUsers.entrySet()) {
			  if (user.getValue().getId() == userId) {
				  return true;
				}
		  }
		return false;
	}
	
	public static boolean userExist(String login) {
		for(Entry<Integer, User> user : StoreData.allUsers.entrySet()) {
			  if (user.getValue().getLogin().equals(login)) {
				  return true;
				}
		  }
		return false;
	}
	
	public int getId() {
		return this.id;
	}
	public String getLogin() {
		return this.login;
	}
}

class Message{
	
	Integer id;
	Integer idEmeteur ;
	Integer idRecepteur;
	String msg;
	String dateReception;
	
	public Message(int id ,Integer idE, Integer idR, String msg) {
			this.idEmeteur = idE;
			this.idRecepteur = idR;
			this.msg = msg;
			this.id=id;
			
			DateFormat dFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			this.dateReception = dFormat.format(new Date());
	}
	
	public Integer getId() {
		return id;
	}

	public Integer getIdEmeteur() {
		return idEmeteur;
	}

	public void setIdEmeteur(Integer idEmeteur) {
		this.idEmeteur = idEmeteur;
	}

	public Integer getIdRecepteur() {
		return idRecepteur;
	}
}

	//Our server
class StoreData{
	static Map<Integer, User> allUsers = new HashMap<>();
	static Map<Integer, Message> allMessages = new HashMap<>();
}