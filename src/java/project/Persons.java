/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author 1895268
 */
@Path("persons")
public class Persons {

    @Context
    private UriInfo context;

   
    public Persons() {
    }

    
    public int insertPersons(int pid,String fname, String lname, String email, String phone,String address,String postal, String studentOrTeacher) {
        
        int result = 0;
        int result2=0;
        int result3=0;
        Connection con=null;
        PreparedStatement stm=null;
        PreparedStatement stm2=null;
        PreparedStatement stm3=null;
        try{
          
         
          DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
          con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
          String sql;
    
            sql="insert into persons values (?,?,?,?,?,?,?)";
            stm=con.prepareStatement(sql);
            stm.setInt(1,pid);
            stm.setString(2, fname);
            stm.setString(3, lname);
            stm.setString(4, email);
            stm.setString(5, phone);
            stm.setString(6, address);
            stm.setString(7, postal);
            result=stm.executeUpdate();
       
            if(studentOrTeacher.equalsIgnoreCase("Asst. Proff")){
                String sql2;
                sql2="insert into teacher values (?,?)";
                stm2=con.prepareStatement(sql2);
                stm2.setInt(1,pid);
                stm2.setString(2, studentOrTeacher);
                result2=stm2.executeUpdate();
            }
            else{
                String sql3;
                sql3="insert into student values (?,?)";
                stm3=con.prepareStatement(sql3);
                stm3.setInt(1,pid);
                stm3.setString(2, studentOrTeacher);
                result3=stm3.executeUpdate();
            }
           stm.close();
           stm2.close();
           stm3.close();
           con.close();
        
          } catch (SQLException ex) {
            Logger.getLogger(Persons.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return result;
        
    }
    
    //
    @GET
    @Path("addPersons&{value1}&{value2}&{value3}&{value4}&{value5}&{value6}&{value7}&{value8}")
    @Produces(MediaType.TEXT_PLAIN)
    public String personInsert(@PathParam("value1") int pid,
            @PathParam("value2") String fname,@PathParam("value3") String lname,
            @PathParam("value4") String email,@PathParam("value5") String phone,
            @PathParam("value6") String address,@PathParam("value7") String postal,
            @PathParam("value8") String studentOrTeacher) {
        
        
        String status=null;
        String message = null;
        
        Instant instant = Instant.now();
        long time = instant.getEpochSecond();
        
        
        JSONObject mainobject=new JSONObject();
        
            int result=insertPersons(pid, fname, lname, phone, email, address, postal, studentOrTeacher);
            if(result> 0){
            status="OK";
            message=result + " updated Successfully";
            mainobject.accumulate("Status :", status);
            mainobject.accumulate("Timestamp :", time);
            mainobject.accumulate("Message :", message);
            }
            else{
            status="Error";
            message=result + " updated Successfully";
            mainobject.accumulate("Status :", status);
            mainobject.accumulate("Timestamp :", time);
            mainobject.accumulate("Message :", message);
            }
         return mainobject.toString();
    }
}
