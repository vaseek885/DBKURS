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
	private static PreparedStatement getsalaries_statement = null;
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
				System.out.println("1 - Add person");
				System.out.println("2 - Update a person's passport info");
				System.out.println("3 - List persons");
				System.out.println("4 - Delete a person");
				System.out.println("5 - Print a list of salaries");
				System.out.println("6 - Exit");
				Scanner sc = new Scanner(System.in);
				Integer in = Integer.parseInt(sc.nextLine());
				draw_menu(in);
				break;
			}
			case 1:{
				add_people();
				break;

			}
			case 2:{
				update_passport();
				break;
			}
			case 3:{
				System.out.println("Reading employees:");
				read_all_people();
				break;
			}
			case 4:{
				delete_person();
				break;
			}
			case 5:{
				print_salaries();
				break;
			}
			case 6:{
				System.exit(0);
				break;
			}
			default: {
				draw_menu(0);
				break;
			}
		}

	}

	private static void print_salaries() {
		open_connection();
        if(connection != null){
            try{
                if(getsalaries_statement != null){
                    ResultSet rs = null;
                    rs = getsalaries_statement.executeQuery();
                    log("After getsalaries_statement queury");  
                    System.out.println("ID, FIRSTNAME, SALARY");
                    while(rs.next()){
                    	

                    	System.out.println(rs.getInt(1) + ", " + rs.getString(2)  + ", " + rs.getInt(3));
                    	

                    }
                    

                } else {
                	log("no statement in getsalaries_statement");
                }
            }catch(Exception e){
                log("Exception in getsalaries_statement");
                log(e.toString());
            }
        }
	}

	private static void read_all_people(){
        open_connection();
        if(connection != null){
            try{
                if(readallpeople_statement!= null){
                    ResultSet rs = null;

                    rs = readallpeople_statement.executeQuery();
                    log("After readallusr_statement queury");  
                    System.out.println("ID, FIRSTNAME, LASTNAME, MIDDLENAME, BIRTH_DATE, SEX, " +
                     "ADDRESS, PHONE_NUMBER, PASPORT(1,1), IMAGE_ID");
                    while(rs.next()){

                    	String pass = null;
                    	if(rs.getObject(9) == null){

                    		pass = "---";
                    	} else {
                    		pass = ((((Struct)rs.getObject(9)).getAttributes())[0]).toString() + " | " + 
                    		((((Struct)rs.getObject(9)).getAttributes())[1]).toString();
                    	}
                    	

                    	System.out.println(rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getString(3) +
                    	  ", " + rs.getString(4) + ", " + rs.getString(5) + ", " + rs.getString(6) +
                    	  ", " + rs.getString(7) + ", " + rs.getString(8) + ", " + pass + ", " + rs.getString(10));
                    	

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

    private static void add_people(){
        open_connection();
        if(connection != null){
            try{
                if(addpeople_statement!= null){

                    ResultSet rs = null;
					
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
						addpeople_statement.setObject(8, connection.createStruct("S208306.PASSPORT", new Object[] { ser,num }));
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

    private static void update_passport(){
        open_connection();
        if(connection != null){
            try{
                if(updatepswrd_statement!= null){

                    ResultSet rs = null;
					Scanner sc = new Scanner(System.in);
					
					System.out.println("Input person ID");
					String out = sc.nextLine();
                	updatepswrd_statement.setInt(2, Integer.parseInt(out));
   					System.out.println("Input password serial number");
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
						updatepswrd_statement.setObject(1, null);
					} else {
						updatepswrd_statement.setObject(1, connection.createStruct("S208306.PASSPORT", new Object[] { ser,num }));
					}
                	updatepswrd_statement.executeUpdate();
                    log("After updatepswrd_statement queury");  
                    
                    

                } else {
                	log("no statement in updatepswrd_statement");
                }
            }catch(Exception e){
                log("Exception in updatepswrd_statement");
                log(e.toString());
            }
        }
        
    }

    private static void delete_person(){
        open_connection();
        if(connection != null){
            try{
                if(deletepeople_statement!= null){

                    ResultSet rs = null;
					Scanner sc = new Scanner(System.in);
					
					System.out.println("Input person ID");
					String out = sc.nextLine();
                	deletepeople_statement.setInt(1, Integer.parseInt(out));
                	deletepeople_statement.executeUpdate();
                    log("After delete_person queury");  
                    
                    

                } else {
                	log("no statement in delete_person");
                }
            }catch(Exception e){
                log("Exception in delete_person");
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

				connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orbis","s208306","lxr868");
                deletepeople_statement = connection.prepareStatement("DELETE FROM PEOPLE WHERE PEOPLE.ID = ?");  
                getsalaries_statement = connection.prepareStatement("SELECT * FROM TABLE (month_stat.get_salaries())");
                readallpeople_statement = connection.prepareStatement("SELECT * FROM PEOPLE"); 
                updatepswrd_statement = connection.prepareStatement("UPDATE PEOPLE SET PASPORT_ = ? WHERE " +
                 "PEOPLE.ID = ?"); 

                addpeople_statement = connection.prepareStatement("INSERT INTO PEOPLE VALUES((SELECT MAX(ID)"+
                " FROM PEOPLE)+1, ?, ?, ?, ?, ?, ?, ?, ?, 1)");  


			} catch (Exception e) {

				System.out.println("Connection Failed! Check log file");
				log(e.toString());

			}
		}
	}
}
