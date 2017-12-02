package com.vsaf.common;

import java.sql.*;
import java.io.*;
import com.vsaf.common.Passport;
import java.util.Scanner;
import java.util.Map;
import java.time.format.*;
import java.time.*;


public class Dbops {
	private static Connection connection = null;





	private static PreparedStatement deletepeople_statement = null;
 	private static PreparedStatement readallpeople_statement = null;
	private static PreparedStatement updatepswrd_statement = null;
	private static PreparedStatement addpeople_statement = null;

	private static void log(String s) {
		PrintWriter outputStream = null;

		try {

			outputStream = new PrintWriter(new FileWriter("logfile", true));
			outputStream.println(s);
			
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	private static void draw_menu(Integer state) {
		switch(state) {
			case 0:{
				System.out.println("Available actions:");
				System.out.println("1 - Add employee");
				System.out.println("2 - Update employee name");
				System.out.println("3 - Read employees");
				System.out.println("4 - Delete employee");
				Scanner sc = new Scanner(System.in);
				Integer in = Integer.parseInt(sc.nextLine());
				draw_menu(in);
				break;
			}
			case 1:{
				System.out.println("Write employee information:");
				
				add_people();
				break;

			}
			case 2:{
				System.out.println("Write employee id");
				Scanner sc = new Scanner(System.in);
				Integer id = Integer.parseInt(sc.nextLine());
				//update_employee(id);
				break;
			}
			case 3:{
				System.out.println("Reading employees:");
				read_all_people();
				break;
			}
			case 4:{
				System.out.println("Write employee id");
				Scanner sc = new Scanner(System.in);
				Integer id = Integer.parseInt(sc.nextLine());
				//delete_employee(id);
				break;
			}
			default: {
				System.exit(0);
				break;
			}
		}

	}

	public static void read_all_people(){
        open_connection();
        if(connection != null){
            try{
                if(readallpeople_statement!= null){
                    ResultSet rs = null;
                    log("readallusr_statement :"+readallpeople_statement.toString());  
                    rs = readallpeople_statement.executeQuery();
                    log("After readallusr_statement queury");  
                    System.out.println("ID, FIRSTNAME, LASTNAME, MIDDLENAME, BIRTH_DATE, SEX, " +
                     "ADDRESS, PHONE_NUMBER, PASPORT(1,1), IMAGE_ID");
                    while(rs.next()){
                    	// Passport pass = null;
                    	String pass = null;
                    	if(rs.getObject(9) == null){
                    		// pass = new Passport(0,0);
                    		pass = "---";
                    	} else {
                    		pass = ((((Struct)rs.getObject(9)).getAttributes())[0]).toString() + " | " + ((((Struct)rs.getObject(9)).getAttributes())[1]).toString();
                    		// pass = new Passport(intValue((((Struct)rs.getObject(9)).getAttributes())[0]),((((Struct)rs.getObject(9)).getAttributes())[1])) ;	
                    	}
                    	

                    	System.out.println(rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getString(3) +
                    	  ", " + rs.getString(4) + ", " + rs.getString(5) + ", " + rs.getString(6) +
                    	  ", " + rs.getString(7) + ", " + rs.getString(8) + ", " + pass + ", " + rs.getString(10));


                    	  // (" + ((Passport)rs.getObject(8)).num1 + "," +
                    	  // + ((Passport)rs.getObject(8)).num2 + "), " + rs.getInt(9));
                    	

                    }

                } else {
                	log("no statement in readallpeople");
                }
            }catch(Exception e){
                log("Exception in readallpeople");
                log(e.toString());
            }
        }
        
    }

    public static void add_people(){
        open_connection();
        if(connection != null){
            try{
                if(addpeople_statement!= null){

                    ResultSet rs = null;

// FIRSTNAME, LASTNAME, MIDDLENAME, BIRTH_DATE, SEX, ADDRESS, PHONE_NUMBER, PASPORT(1,1), IMAGE_ID
					
					Scanner sc = new Scanner(System.in);
					System.out.println("Input first name");
					String out = sc.nextLine();
                	addpeople_statement.setString(1, out);
                	System.out.println("Input last name");
                	out = sc.nextLine();
                    addpeople_statement.setString(2, out);
                    System.out.println("Input middle name");
                	out = sc.nextLine();
                    addpeople_statement.setString(3, out);
                    System.out.println("Input birth date in format yyyy/MM/dd");
                	out = sc.nextLine();

					DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
					LocalDate date = LocalDate.parse(out, format);
                    addpeople_statement.setDate(4, java.sql.Date.valueOf(date));

                    System.out.println("Input sex");
                	out = sc.nextLine();
                	addpeople_statement.setString(5, out);
                    System.out.println("Input adress");
                	out = sc.nextLine();
					addpeople_statement.setString(6, out);
					System.out.println("Input phone number");
                	out = sc.nextLine();
					addpeople_statement.setString(7, out);
					System.out.println("Input passport serial number");
					out = sc.nextLine();
					Integer ser = 0;
					if(out == "0") {
                		ser = null;
                	} else {
                		ser = Integer.parseInt(out);
                	}

					
					System.out.println("Input passport number");
					Integer num = 0;
                	out = sc.nextLine();
                	if(out == "0") {
                		num = null;
                	} else {
                		num = Integer.parseInt(out);
                	}
                	if(num == null|| ser == null){
						addpeople_statement.setObject(8, null);
					} else {
						addpeople_statement.setObject(8, connection.createStruct("PASSWORD", new Object[] { ser,num }));
					}
                	addpeople_statement.executeUpdate();
                    log("After addpeople_statement queury");  
                    
                    

                } else {
                	log("no statement in add_people");
                }
            }catch(Exception e){
                log("Exception in add_people");
                log(e.toString());
            }
        }
        
    }

	public static void main(String[] args) {

		try{
			FileWriter fw = new FileWriter("logfile");
			fw.close();
		} catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		draw_menu(0);


	}


	private static void open_connection(){


		if(connection == null){

			try {   

				Class.forName("oracle.jdbc.driver.OracleDriver");

			} catch (ClassNotFoundException e) {

				log("No Oracle JDBC Driver?");
				System.out.println("Something went wrong with the connection, check log file");
				log(e.toString());

			}

			log("PostgreSQL JDBC Driver registering");

			try {

            	// String tablename = "lul"; // temp

				connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orbis","s208306","lxr868");

                // String insertTableSQL = "INSERT INTO " + tablename 
                //         + " (x, y, r, contained) VALUES "
                //         + "(?,?,?,?)";

				Map map = connection.getTypeMap();
                map.put("SchemaName.passport", Class.forName("com.vsaf.common.Passport"));
                connection.setTypeMap(map);

                // add_statement = connection.prepareStatement(insertTableSQL);  
                deletepeople_statement = connection.prepareStatement("DELETE FROM PEOPLE WHERE PEOPLE.ID = ?");  

                readallpeople_statement = connection.prepareStatement("SELECT * FROM PEOPLE"); 
                updatepswrd_statement = connection.prepareStatement("UPDATE PEOPLE SET PASPORT_ = PASSPORT(1,2) WHERE " +
                 "PEOPLE.ID = ?"); 
                // updateall_statement = connection.prepareStatement("UPDATE " + tablename + " SET r = ?"); 

                addpeople_statement = connection.prepareStatement("INSERT INTO PEOPLE VALUES((SELECT MAX(ID) FROM PEOPLE)+1, ?, ?, ?, ?, ?, ?, ?, ?, 1)");  

                // finduser_statement = connection.prepareStatement("SELECT * FROM " + tablename + 
                // " WHERE name = ? AND password = ?");  

			} catch (Exception e) {

				System.out.println("Connection Failed! Check log file");
				log(e.toString());

			}
		}
	}
}
