package com.Projec.WiZBin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class WizBin {
	
		@Autowired	
		JdbcTemplate jdc;
		
		@GetMapping("/api/SignUp")
		public String signupInfo(String mobileNumb,String password,String email)throws SQLException {
			if(mobileNumb!=null) {
			Pattern p=Pattern.compile("(0/91)?[6-9][0-9]{9}");
			Matcher m=p.matcher(mobileNumb);	
			System.out.println(mobileNumb);
			if(m.find()==true) {		
		    int i=	jdc.update("insert into signup_info(mobileno,password)values(?,?)",new Object[]{mobileNumb,password});	
	     	  if(i>0) {
	     	String s=createrandomUrl(5);
	     	 int j=	jdc.update("insert into customer_info(cust_id) values(?)",new Object[] {s});
	    
	     	 if(j>0) {
	     		 return "SignUp Successfully and your customer_id is " +s ;
	     	 }     	
	     		
	     	  }
			}
			else 
			return "enter valid mobile number";	
			}
			
			else {		
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce","root","priyanka@8949");
		Statement stm=con.createStatement();
		String query="select * from  signup_info where email='"+email+"' ";
		ResultSet rs=stm.executeQuery(query);	
		if(rs.next()) {
//			String pwd=rs.getString("password");
//			if(pwd.equals(password))  
				return "you already signup with this email try another one";
		}
			else {		
				PreparedStatement stm1= con.prepareStatement("insert into signup_info (password,email)values(?,?)");
				stm1.setString(1, password);
				stm1.setString(2, email);
				 int i=stm1.executeUpdate(); 
				 if(i>0)
				 {
				  String s=createrandomUrl(5);			 
		     	 int j=	jdc.update("insert into customer_info(cust_id) values(?)",new Object[] {s});
		     	 if(j>0) {
		     		 return "SignUp Successfully and your customer_id is"+s ;
		     	 }
					 
				 }	
		}	
			}
					catch (Exception e) { 					
						e.printStackTrace();
					}
			}

			return "null";			
		}


	@GetMapping("/api/LogIn_info")
			
			public String loginInfo(String email,String password,String mobileNumb)
			{
		if(email!=null) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=	DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce", "root", "priyanka@8949");
					Statement st=con.createStatement();
					String query="select password from  signup_info where email='"+email+"' ";
					ResultSet rs=st.executeQuery(query);
						if(rs.next()) {
						String pwd=rs.getString("password");
						if(pwd.equals(password)) 
							return "you are valid user";				
							else					
								return " please enter valid password";	
					}				
							else					
								return"your are not registered, register first";				
					
					}
				 catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}	
		if(mobileNumb!=null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=	DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce", "root", "priyanka@8949");
				Statement st=con.createStatement();
				String query="select password from  signup_info where  mobileno='"+mobileNumb+"' ";
				ResultSet rs=st.executeQuery(query);
					if(rs.next()) {
					String pwd=rs.getString("password");
					if(pwd.equals(password)) 
						return "you are valid user";				
						else					
							return " please enter valid password";	
				}				
						else					
							return"your are not registered, register first";				
				
				}
			 catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			return password;
			}	


	public static String createrandomUrl(  int targetStringLength ) {
	int leftLimit = 48; // numeral '0'
	int rightLimit = 122; // letter 'z'

	Random random = new Random();

	String generatedString = random.ints(leftLimit, rightLimit + 1)
	  .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	  .limit(targetStringLength)
	  .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	  .toString();

	return generatedString;
	}	




	@GetMapping("/api/InsertCustomer_info/")
	public static String custInfo(String custId,String name,String mobileNumb,int age)
	{
		if(custId!=null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=	DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce", "root", "priyanka@8949");
				Statement st=con.createStatement();
				String query="select * from  customer_info where cust_id='"+custId+"' ";
				ResultSet rs=st.executeQuery(query);
					if(rs.next()) 
					{
					String id=rs.getString("cust_id");
					if(id.equals(custId)==true)
					{
						if(mobileNumb!=null&&name!=null&&age>12) 
						{
							Pattern p=Pattern.compile("(0/91)?[6-9][0-9]{9}");
							Matcher m=p.matcher(mobileNumb);	
							System.out.println(mobileNumb);
							if(m.find()==true)
							{		
				      PreparedStatement stm= con.prepareStatement("update customer_info set mobileno=?  where cust_id='"+custId+"'");
						stm.setString(1, mobileNumb);
						 int i=stm.executeUpdate(); 
							}					
							else {
								return "enter valid mobile number";	
							}
						PreparedStatement stm1= con.prepareStatement("update  customer_info set   age=?  where cust_id='"+custId+"'");				
						stm1.setInt(1, age);
						 int j=stm1.executeUpdate();
						PreparedStatement stm2= con.prepareStatement("update  customer_info set   Name=?  where cust_id='"+custId+"'");
						stm2.setString(1, name);
						 int i=stm2.executeUpdate(); 
						 if(i>0&&j>0)
							 return "Welcome to Our Ecommerce website ";				
						
					}
						else 
							return "insert all entries";					
					}				
					}				
				}		
		 catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
		}
	}
		     else 
			 return "enter customer_id First";
		     
		     
		return "enter valid customer id";     

	}






	//api for forget password
	@GetMapping("/api/forget_password")
	public static String forget(String email)
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce","root","priyanka@8949");
	Statement stm=con.createStatement();

			Random randn=new Random();
			int rand=randn.nextInt(10000);		
			PreparedStatement stmt = con.prepareStatement("insert into forgetPassword(otp,email)values(?,?)");
			stmt.setInt(1,rand);
			stmt.setString(2, email);
			int i = stmt.executeUpdate();
			System.out.println("Number of rows inserted "+i);
			String data=email;
			if(i>0)
			EmailSend.email("raopriyanka2205@gmail.com", "snfafqflwlbffwet",data, "otp to reset password",Integer.toString(rand));
			return"your otp is succesfully sended";	
		}     
		
	catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "otp";
		
	}

	//api for otp check
	@GetMapping("/api/for-otp-confirmation")
	public static String otpConfirm(int otp1)
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=	DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce", "root", "priyanka@8949");
			Statement st=con.createStatement();
			//int s=Integer.parseInt(otp);
			String query="select*from forgetPassword where otp='"+otp1+"'" ;
			ResultSet rs=st.executeQuery(query);
			if(rs.next())
			{
				 int data=rs.getInt("otp"); 
				if(data==otp1)			
				   return "otp is correct";		
				
			}
		}
		catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "otp not found enter right opt ";
		
	}


	//api for ResetPassword

	@GetMapping("/api/ResetPAssword")
	public static String reset(String email,String password, String mobileNumb)
	{
	 if(email!=null) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce","root","priyanka@8949");
	Statement stm=con.createStatement();				
			PreparedStatement stmt = con.prepareStatement("update signup_info set password=? where email=?");	
			stmt.setString(1,password);
			stmt.setString(2,email);
			int i = stmt.executeUpdate();
			System.out.println("Number of rows inserted "+i);	
			if(i>0)		
			return"your password updated successfully";	
		}     
		
	catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "mail id not found";
	 }
	 if(mobileNumb!=null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce","root","priyanka@8949");
		Statement stm=con.createStatement();				
				PreparedStatement stmt = con.prepareStatement("update signup_info set password=? where mobileno=?");	
				stmt.setString(1,password);
				stmt.setString(2,mobileNumb);
				int i = stmt.executeUpdate();
				System.out.println("Number of rows inserted "+i);	
				if(i>0)		
				return"your password updated successfully";	
			}     
			
		catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "mobile number  not found";
	 }
	 return "enter valid info";
	}



	//api for product categories	
		@GetMapping("/api/Search_by_Categories/")
		
		public static Map showData() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce","root","priyanka@8949");
			PreparedStatement stm= con.prepareStatement("select * from Shope_By_Category");				
			ResultSet rs=stm.executeQuery();
			ArrayList list=new ArrayList();
			while(rs.next()) {
				Map map=new HashMap();
				map.put("Category_id", rs.getString("Category_id"));
				map.put("Category_Name", rs.getString("Category_Name"));
				list.add(map);		
			}			
				Map newMap=new HashMap();
				newMap.put("Categories_details", list);
				newMap.put("Status", "ok");			
				return newMap;			
		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}		
			return null;
		}



	//Search By category name or by id
		

	@GetMapping("/api/Search/Products/ByAvailableCategories/{Catg_id}")
	public static Map findByCatgid(@PathVariable("Catg_id")int catgId ) {
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Ecommerce","root","priyanka@8949");
				PreparedStatement stm=con.prepareStatement("select*from Products a join Shope_By_Category b on a.Catagory_id=b.Category_id where Catagory_id='"+catgId+"' ");			
				ResultSet rs=stm.executeQuery();
				ArrayList list=new ArrayList();	
				while(rs.next()) {
					Map map=new HashMap();
					map.put("Catagory_id", rs.getString("Catagory_id"));
					map.put("product_available", rs.getString("product_available"));
					map.put("Category_Name", rs.getString("Category_Name"));
					map.put("Discount", rs.getString("Discount"));
					
					list.add(map);		
				}			
					Map newMap=new HashMap();
					newMap.put("Details", list);
					newMap.put("status", "ok");			
					return newMap;				
			} 
				catch (ClassNotFoundException|SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 		
			return null;
		}



	@GetMapping("/api/addNewProduct")
	public void  addNewProduct(String productName ,String productDescription,Double productDiscountedPrice,Double productActualPrice) {
		 int i=	jdc.update("insert into Product_details(productName,productDescription,productDiscountedPrice,productActualPrice)values(?,?,?,?)",
			new Object[] {productName,productDescription,productDiscountedPrice,productActualPrice});	
		
	}

	@GetMapping("/api/UpdateProduct")
	public String updateProduct( Integer productId,String productName ,String productDescription,Double productDiscountedPrice,Double productActualPrice) {
		if(productId!=null) {
		 int i=	jdc.update("update Product_details set productName=?,productDescription=?,productDiscountedPrice=?,productActualPrice=? where product_id='"+productId+"'",
			new Object[] {productName,productDescription,productDiscountedPrice,productActualPrice});	
		 return "data is successfully updated";
		}
		return "insert valid product_Id";
	}


	@GetMapping("/Show_product_details")
	public Object selectData(Integer productId) {
	List<Map<String, Object>> url=	jdc.queryForList("Select*from Product_details where product_id='"+productId+"'");
	return url;
	}



	@GetMapping("/AddToCart")
	public String addToCart( Integer productId) {
		if(productId!=null) {
		 int i=	jdc.update("insert into addToCart (product_id, productName,productDiscountedPrice,productActualPrice)\r\n"
		 		+ "( select product_id, productName,productDiscountedPrice,productActualPrice from Product_Details where product_id='"+productId+"')");	
		 return "Product Successfully added to cart";
		}
		return "somthing went wrong";
	}




	@GetMapping("/RemoveFromCart")
	public String removeFromCart( Integer productId) {
		if(productId!=null) {
		 int i=	jdc.update("delete from addToCart where product_id='"+productId+"' ");	
		 return "Successfully remove from cart";
		}
		return "somthing went wrong";
	}

	@GetMapping("/BuyNow")
	public String orderPlace(Integer productId ,String custId  ,String  state , String address ,
			 Integer pincode  ,String paymentMethod ,String size ,Integer quantity,String mobileNumb)
	{
		if( productId!=null && custId !=null&& state !=null&& address!=null&&
				pincode!=null&& paymentMethod!=null&& size!=null&& quantity!=null&&mobileNumb!=null)
		{
			Random randn=new Random();
			long rand=randn.nextLong(1000000000);
				Pattern p=Pattern.compile("(0/91)?[6-9][0-9]{9}");
				Matcher m=p.matcher(mobileNumb);	
				if(m.find()==true) {
				int k=	jdc.update("insert into  Order_details values(?,?,?,?,?,?,?,?,?,'ordered',current_date(),?)", new Object[]
						{rand,productId,custId, state,address, pincode, paymentMethod, size,quantity, mobileNumb,});			
				if(k>0)
					return"Ordered Successfully";
				else
					return "enter valid details";
				}
		}	
		return "please fill all  details";
	}


	@GetMapping("/api/My-order_details")
	public Object myOrders( String custId)
	{
		List<Map<String, Object>> list=	jdc.queryForList("Select*from Order_details where cust_id='"+custId+"'");	
		return list;
	}

	@GetMapping("/api/Cancelorder")
	public String cancelOrder(Integer orderId) {
	int i=	jdc.update("update  Order_details set order_status='Canceled' where order_id='"+orderId+"'");
	    if(i>0) {
	    	return"canceled Successfully";
	    }
	    return "please check your network connection";
	}


	@GetMapping("/api/LogOut")
	public Object logOut( String custId)
	{
		int i=	jdc.update("delete from customer_info where cust_id='"+custId+"'");
		if(i>0)
			return "logOut successful";
		else
			return"please check your connection";
	}

	}


