package New;

import java.util.Scanner;
import java.sql.*;
//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.io.* ;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Date;

public class Myclass {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		try {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pglab","root","12345");
		Statement st = con.createStatement();
		con.setAutoCommit(false) ;
       
		String sid ;
		String Coursename;
		String Courseid ;
		String Grade ;
		String cid ;
		String f_id ;
		String id;
		String grade ;
		System.out.println("1. faculty \n 2. student \n 3. academic");
		System.out.println("enter input:- ");
		Scanner sc = new Scanner(System.in);
		int input = sc.nextInt();
		switch(input)
		{
		case 1 : System.out.println("faculty :- \n ");
		   
		System.out.println("enter faculty id:- \n");
		 f_id = sc.next() ;
		 System.out.println("Enter The Password : \n") ;
		 String pass = sc.next() ;
		 
		 
		 
		 
		ResultSet set;
		set = st.executeQuery("select * from LoginPass Where ID = \"" +  f_id  + "\"  AND Pass =  \"" + pass  +"\";") ;
	    
		if(set.next())
		{
			System.out.println("Login Succesfull - - Welcome");
			
			
			//*****************************************************************
			
			SimpleDateFormat timer = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			 Date date = new Date();
			 String str= timer.format(date);
			 
			 String time_insert = "insert into Time_table(user_id,user_time) values(?,?)";
			 PreparedStatement stm = con.prepareStatement(time_insert);
			 
			 stm.setString(1,f_id);
			 stm.setString(2,str);
			 
			 stm.executeUpdate();
			
			
			//*****************************************************************
			
			
			System.out.println("1. Grade sheet of student  \n 2. CSV Grade Updates \n 3. grade entry \n 4.View list of student \n 5. View offered courses  \n 6 . log out ");
			
			System.out.println("Enter your Choice \n") ;
			int choice = sc.nextInt() ;
			
			switch(choice)
			{
			case 1 :
				
				System.out.println("Enter the Student id for which you want to view grade sheet:"  ) ;
				 sid = sc.next() ; 
				set =st.executeQuery( " select coursename , s.courseid , grade from catalog c , studentcourse s where studentid= \"" + sid + "\" and c.courseid = s.courseid ") ;
                set.next() ;
				do
				{
					 Coursename = set.getString("coursename");
					 Courseid = set.getString("courseid");
					 Grade = set.getString("grade");
					System.out.print(Coursename) ;
					System.out.print( " ") ;
					System.out.print(Courseid) ;
					System.out.print( " ") ;
					System.out.print(Grade) ;
					System.out.print( "\n ") ;
				}while(set.next());
				con.commit();

				break ; 
			case 2 :
				int batchSize = 100 ; 
		        String csvFilePath = "C:\\Users\\hp\\Desktop\\Sample.csv";
				
		        BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
	            String lineText = null;
	            int count = 0;
	            lineReader.readLine();
	            while ((lineText = lineReader.readLine()) != null) {
	                String[] data = lineText.split(",");
	                String Studentid = data[0];
	                String Register = data[1];
	                String Semester = data[2];
	                String courseid = data[3];
	                 grade = data[4];
	                String cgpa = data.length == 6 ? data[5] : "";
	            String test ; 
	            ResultSet Newset;
	            test = "Select * FROM studentcourse WHERE Studentid = \""+ Studentid +"\" AND courseid  = \"" + courseid +  "\" ;" ;
	            //System.out.println(test);  
	            
	              Newset=st.executeQuery(test);
	              String sql;
	              if(!Newset.next()) {
		       
			
		         sql = "INSERT INTO studentcourse (Studentid,Register,Semester,courseid,grade,cgpa) VALUES (?, ?, ?, ?, ?,?)";
			
		        PreparedStatement statement = con.prepareStatement(sql);
	                statement.setString(1, Studentid);
	                statement.setString(2, Register);
	                statement.setString(3, Semester);
	                statement.setString(4, courseid);
	 
	                /*Timestamp sqlTimestamp = Timestamp.valueOf(timestamp);
	                statement.setTimestamp(3, sqlTimestamp);
	 
	                Float fRating = Float.parseFloat(rating);
	                statement.setFloat(4, fRating);*/
	 
	                statement.setString(5, grade);
	                statement.setString(6, cgpa);
	                statement.addBatch();
	 
	                if (count % batchSize == 0) {
	                    statement.executeBatch();
	                }
	            
	            statement.executeBatch() ;
	            }
	                else
	                {
	                	sql = "UPDATE studentcourse SET grade = ? , cgpa = ?  WHERE Studentid = ? AND courseid = ?" ;

	    		        PreparedStatement statement = con.prepareStatement(sql);
	    		        statement.setString(1, grade);
	    		        statement.setString(2, cgpa);
		                statement.setString(3, Studentid);
		                statement.setString(4, courseid );
		                statement.addBatch();
		           	 
		                if (count % batchSize == 0) {
		                    statement.executeBatch();
		                }
		            
		            statement.executeBatch() ;
		 
	                	
	                	
	                }
	                }
	 
	            lineReader.close();
	            // execute the remaining queries
	            System.out.println("Connection Closed") ;
	            con.commit();	 
                break ; 
           
			case 3 :
				
				while(true) 
				{
					System.out.println("Enter the Student id and course id for which you want to enter the grade");
				    
					sid = sc.next();
			
					cid = sc .next() ;
					
					System.out.println(" 1  for Register or 2  for Deregister") ;
					int Reg_Der = sc.nextInt() ;
					if(Reg_Der == 1)
					{
						System.out.println("Enter a grade: ") ;
						grade = sc.next();
						String query;
					
					       query = "INSERT INTO studentcourse VALUES (?,?,?,?,?)";
						
						PreparedStatement preparedStmt = con.prepareStatement(query);
						/*preparedStmt.setString("sid","register","3rd","cid","grade");*/
						
						preparedStmt.executeUpdate();
						
						set =st.executeQuery( "insert into studentcourse values(" +sid +" , \"Register\" , \"NULL\"," +cid +"," + grade + ");"   ) ;
		                set.next() ;
		                con.commit() ;
						
					}
					else
					{}
					
					
					set =st.executeQuery( "  ") ;
	                set.next() ;
					
					
					System.out.println("Do you Want to Exit the Grade entry : 1 for Yes  / 2 for No ");
					int exit = sc.nextInt();
					if(exit == 1)
					{break ; }
					
					
	
				
				}
                break ; 
			case 4 :
				System.out.println("list of student :- ");
				
				sid = sc.next() ; 
				set =st.executeQuery( " select  from  studentcourse s where studentid= \"" + sid + "\" and c.courseid = s.courseid ") ;
                set.next() ;
				do
				{
					 Coursename = set.getString("coursename");
					 Courseid = set.getString("courseid");
					 Grade = set.getString("grade");
					System.out.print(Coursename) ;
					System.out.print( " ") ;
					System.out.print(Courseid) ;
					System.out.print( " ") ;
					System.out.print(Grade) ;
					System.out.print( "\n ") ;
				}while(set.next());
				
				
				
				
				
                break ; 
			case 5 :
				
				
				System.out.println("You are Offering the Following Courses :"  ) ;
				set =st.executeQuery( " select coursename , f.courseid from catalog c , facultycourse f where facultyid= \"" + f_id + "\" and c.courseid = f.courseid ") ;
                set.next() ;
				
				
				do
				{
				
					 Coursename = set.getString("coursename");
					 Courseid = set.getString("courseid");
				
				
					System.out.print(Coursename) ;
					System.out.print( " ") ;
					System.out.print(Courseid) ;
					System.out.print( " ") ;
					System.out.print( "\n ") ;
				}while(set.next());
				
				con.commit() ;
				
				
				
                break ; 
			case 6 :
				System.out.print("logout") ;
				con.close() ;
                break ; 
           default : 
        	   
			}

			/*String Sql ;
			Sql = "Select * from Faculty" ;
			ResultSet rs ;
			rs = st.executeQuery("Select * from Faculty " ) ; 
			rs.next();
			
			int roll;
			String name ;
			String course ;
			
			do
			{
			
				 roll = rs.getInt("FacultyID");
				 name = rs.getString("Fname");
				 course = rs.getString("CourseID");
			
			
				System.out.print(roll) ;
				System.out.print( " ") ;
				System.out.print(name) ;
				System.out.print( " ") ;
				System.out.print(course) ;
				System.out.print( "\n ") ;
				
			}while(rs.next());*/
			
		}
		else
		{
			System.out.println("Log in - Unsucessfull") ;
		}
		 
		st.close() ;
		con.close() ;
		
		
			break;
			
	//***************************************************************************************************		
		
			
		case 2: System.out.println("student section :-");
	           	

		System.out.println("enter student id:- \n");
		 sid = sc.next() ;
		 System.out.println("Enter The Password : \n") ;
		 pass = sc.next() ;
		 
			set = st.executeQuery("select * from LoginPass Where ID = \"" +  sid  + "\"  AND Pass =  \"" + pass  +"\";") ;
			if(set.next())
			{
		    System.out.println(" 1.view my gradesheet \n 2. compute CGPA \n 3. Register for course \n 4. exit");
		   
		    System.out.println("enter your choice :-");
		    int ch = sc.nextInt();
		    
		    switch(ch)
		    {
		    case 1: 
		    	
		    	ResultSet rst;
				rst = st.executeQuery("select courseid , grade from studentcourse where Studentid=\""+sid+"\";");
				rst.next();
				String std_courseid ;
				String std_grade ;
				
				do
				{
					 std_courseid = rst.getString("courseid");
					 std_grade = rst.getString("grade");
				
					System.out.print(std_courseid) ;
					System.out.print( " ") ;
					System.out.print(std_grade) ;
					System.out.print( "\n ") ;
					
				}while(rst.next());
				
				
				
		    	break;
		    case 2:
		    	
		    	ResultSet rgt;
		    	rgt = st.executeQuery("select  s.studentid , (SUM(credit*s.cgpa))/(SUM(credit)) from studentcourse s, catalog c WHERE s.studentid=\""+ sid +"\" Group BY s.studentid,c.courseid ;");

		    	rgt.next();
		    	 
		    	String std_id ;
				String cgpa ;
				
				do
				{
					 std_id = rgt.getString("s.studentid");
					 cgpa = rgt.getString("(SUM(credit*s.cgpa))/(SUM(credit))");
				
					System.out.print(std_id) ;
					System.out.print( " ") ;
					System.out.print(cgpa) ;
					System.out.print( "\n ") ;
					
				}while(rgt.next());
		    	
		    	
		    	
		    	
		    	break;
		    case 3:
		    	System.out.println("Enter the course id " ) ;
		    	String courseid = sc.next() ;
		    	
		    	

		    	System.out.println("Enter the Semester " ) ;
		    	String Semester = sc.next() ;
		    	
		    	 set = st.executeQuery( "INSERT Into studentcourse values( \""+sid+"\" , \"Registered\" , \""+Semester+"\" , \"" +courseid+"\"  )") ;
		    	 
		    	 set.next() ;
		    	 
		    	 
		    	 
		    	break;
		    	
		    }
		    
		    }
			else {
				System.out.println("Student login Uncessfull"); 
			}
			break;
		case 5 :
			System.out.println("Exit") ;
			con.close() ;
			break ;
			
			
		case 3 :
			  
			 System.out.println("Acadamic section :-");
	           	

				System.out.println("enter student id:- \n");
				 sid = sc.next() ;
				 System.out.println("Enter The Password : \n") ;
				 pass = sc.next() ;
				 
					set = st.executeQuery("select * from LoginPass Where ID = \"" +  sid  + "\"  AND Pass =  \"" + pass  +"\";") ;
					
					if(set.next())
					{
						
						System.out.println("Admin Login Success");
						
						 System.out.println("1. Logout");
						 System.out.println("2. Create Course");
						 System.out.println("3. Viewing List of all student ");
						 System.out.println("4. Generate transcript for a student");
						
					     int choice = sc.nextInt();
						 switch(choice){
						 case 1:
						 System.out.println("Logout Successfully");
						 con.close();
						 break;
						 case 2:
						 System.out.println("Create Course");
						 System.out.println("1 .Entry of Course");
						 System.out.println("2 .Removal of Course");
						 int choice3 = sc.nextInt();
						
						 if(choice3 == 1)
						 {
						 System.out.println("Enter course ID : ");
						 String c_id1 = sc.next();
						 System.out.println("Course Name : ");
						 String c_name1 = sc.next();
						 System.out.println("Pre Requisite ");
						 String pre_req1 = sc.next();
						 System.out.println("Enter Credit Value:");
						 String cred = sc.next() ;
						 System.out.println("Enter L value : ");
						 String l1 = sc.next();
						 System.out.println("Enter T value : ");
						 String T1 = sc.next();
					     System.out.println("Enter P value : ");
						 String P1 = sc.next();
						
						
						 String coursecatalogpdst = "insert into catalog(courseid , coursename, prereq, credit,L, T, P) values(?,?,?,?,?,?,?)";
						 PreparedStatement statement = con.prepareStatement(coursecatalogpdst);
						 statement.setString(1, c_id1);
						 statement.setString(2,c_name1);
						 statement.setString(3,pre_req1);
						 statement.setString(4 , cred);
						 statement.setString(5, l1);
						 statement.setString(6, T1);
						 statement.setString(7, P1);
						 statement.executeUpdate();
						 System.out.println("New Course Added Sucessfully");
						
						 }
						 else if(choice3 == 2)
						 {
						 System.out.println("Enter course ID : ");
						 int c_id11 = sc.nextInt();
						
						 PreparedStatement statement = con.prepareStatement("delete  from  catalog Where course_Id = ? ");
						 statement.setInt(1, c_id11);
						
						 statement.execute();
						 System.out.println("Course Removed Successful");
						 }
						break;
						 case 3:
						 System.out.println("View List of a student");
						
						 set=st.executeQuery("select * from studentcourse");    
						
						 while(set.next())
						 {
						int rollno1 = set.getInt("StudentID");
						 String Name1=set.getString("Register");
						 String department1=set.getString("Semester");
						 String course1=set.getString("courseid");
						 String grade1 = set.getString("grade");
						 String cgpa = set.getString("cgpa") ;
						
						
						 System.out.println(rollno1 +" " + Name1 + " " + department1 +" " + course1+" "+grade1 +  " " +cgpa);
						 }
						
						 break;
						 case 4 :
						 System.out.println("Generate transcript for a Student");
						 
						 System.out.println("Enter the Student id ") ;
						 String StudentID = sc.next();
						 
						 set = st.executeQuery("Select * from studentcourse where Studentid = \"" + StudentID + "\";");
						 
						 FileWriter fstream = new FileWriter("C:\\Users\\hp\\Desktop\\Transcript.txt");
						 BufferedWriter out = new BufferedWriter(fstream);
						 while (set.next()) {            
						         out.write(set.getString("Studentid") + ", ");
						         out.write(set.getString("Register") + ", ");
						         out.write(set.getString("Semester") + ", ");
						         out.write(set.getString("courseid") + ", ");
						         out.write(set.getString("grade") + ", ");
						         out.write(set.getString("cgpa") + ", ");
						         out.newLine();
						         /*out.write(System.getProperty("line.separator"));*/
						 }
						 System.out.println("Completed writing into text file");
						 out.close();
						 
						 
						
						 break;
						 }
						
					
						 }
						 else
						 {
						 System.out.println("Admin login Not Success");
						}
						 }
//						 else if(firstchoice == 2){
//						 System.out.println("Enter Username : ");
//						 String newusername2 = sc.next();
//						 System.out.println("Enter Password : ");
//						 String newpassword2 = sc.next();
//						
//						 String newfacultyinserting = "insert into adminlogin(username, password) values(?,?)";
//						 PreparedStatement prst = con.prepareStatement(newfacultyinserting);
//						 prst.setString(1, newusername2);
//						 prst.setString(2,newpassword2);
//						
//						 prst.executeUpdate();
//						 System.out.println("New Admin Registered Sucessfully");
//						
//						
//						}
//						
					
		
		}
		catch(Exception e)
		{
			System.out.println("error:"+ e.getMessage() ) ;
		}

	}
	

}
