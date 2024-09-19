import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class BookRent extends JPanel{
	DefaultTableModel model=null;
	JTable table=null;
	
	String query=null;
	
	Connection con=null;
	Statement stmt=null;
	ResultSet rs = null;  // select결과를 fetch하는 객체
	
	JButton btnReset=null;
	JButton btnRent=null;
	JButton btnReturn=null;
	
	String clickedID=null;
	String clickedTitle=null;
	String clickedDate=null;
	
	
	
	public BookRent() {
		query="select student.id, student.name, book.title, rentbook.rdate "
				+ " from student,rentbook,book"
				+ " where student.id=rentbook.id"
				+ " and rentbook.bid=book.bid order by rentbook.rdate";
		
		// Layout default: FlowLayout
		this.setLayout(null); // layout사용안함. 컴포넌트의 위치.크기 직접 설정
		
		this.setBackground(new Color(57,40,28));
		
		JLabel lblDepartment=new JLabel("Department");
		lblDepartment.setBounds(260, 10, 70, 20); // "학과"lable의 위치와 크기 지정
		lblDepartment.setForeground(Color.WHITE);
		this.add(lblDepartment);
		
		
		
		
		JLabel lblRefresh = new JLabel("Refresh");
		lblRefresh.setBounds(630,13,60,20);
		lblRefresh.setForeground(Color.WHITE);
		this.add(lblRefresh);
		ImageIcon imgRefresh = new ImageIcon("img/refresh.png");
		Image img3 = imgRefresh.getImage();
//		ImageIcon imgRefresh = new ImageIcon(getClass().getClassLoader().getResource("refresh.png"));
	
		Image scaledImg = img3.getScaledInstance(40, 30, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImg);
		
		btnReset=new JButton(scaledIcon);
		btnReset.setVerticalTextPosition(JButton.BOTTOM);
		btnReset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				list();
			}
		});
		btnReset.setBounds(690,5,40,30);
		this.add(btnReset);
		
		this.setSize(780, 530);
		this.setVisible(true);
		
		//ComboBox만들기
		String[] dept={"Total","ComputerScience","Mathematics","Biology","English","Nursing","Business","Accounting","Art","Engineering"}; // 목록
		JComboBox cbDept=new JComboBox(dept);
		
		cbDept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//기본쿼리
				query ="select student.id,student.name,book.title,rentbook.rdate"
						+ " from student,book,rentbook"
						+ " where student.id = rentbook.id"
						+ " and book.bid = rentbook.bid";
				JComboBox cb=(JComboBox)e.getSource();
				
				// 동적쿼리작성, 동적쿼리: 실행시에 쿼리가 바뀌는 것
				if(cb.getSelectedIndex()==0) { //전체
					query+=" order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==1) { //국문학과
					query+=" and student.dept='ComputerScience' order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==2) { //물리학과
					query+=" and student.dept='Mathematics' order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==3) { //컴퓨터공학과
					query+=" and student.dept='Biology' order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==4) { //컴퓨터공학과
					query+=" and student.dept='English' order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==5) { //컴퓨터공학과
					query+=" and student.dept='Nursing' order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==6) { //컴퓨터공학과
					query+=" and student.dept='Business' order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==7) { //컴퓨터공학과
					query+=" and student.dept='Accounting' order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==8) { //컴퓨터공학과
					query+=" and student.dept='Art' order by rentbook.rdate";
				}else if(cb.getSelectedIndex()==9) { //컴퓨터공학과
					query+=" and student.dept='Engineering' order by rentbook.rdate";
				}	
//				System.out.println(query);
				
				
				
				list(); //목록출력
				
			}});
		
		cbDept.setBounds(345, 10, 150, 20);
		this.add(cbDept);
		
		String colName[]={"StudentID","Name","Book Title","Loan Record"};
		model=new DefaultTableModel(colName,0);
		table=new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(760,350));
//		this.add(table);
		JScrollPane sp=new JScrollPane(table);
		sp.setBounds(10, 40, 760, 350);
		
		this.add(sp);
		
		//전체
		list();	
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				table=(JTable) e.getComponent(); // 컴포넌트를 JTabel로 변환
				model=(DefaultTableModel) table.getModel();
				clickedID=(String)model.getValueAt(table.getSelectedRow(), 0); // 선택한 행의 id구하기, 열 위치
				clickedTitle=(String)model.getValueAt(table.getSelectedRow(), 2);
				clickedDate=(String)model.getValueAt(table.getSelectedRow(), 3);
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
					
		JLabel lblRent = new JLabel("Loan");
		lblRent.setBounds(335,410,80,20);
		lblRent.setForeground(Color.WHITE);
		this.add(lblRent);
		ImageIcon imgRent = new ImageIcon("img/rent.png");
		Image img = imgRent.getImage(); // 이미지 가져오기
		Image resizedImg = img.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH); // 버튼 크기에 맞게 이미지 크기 조정
		ImageIcon resizedIcon = new ImageIcon(resizedImg); // 조정된 이미지로 새로운 ImageIcon 생성
		
//		ImageIcon imgRent = new ImageIcon(getClass().getClassLoader().getResource("rent.png"));
//		btnRent=new JButton(imgRent);
		btnRent = new JButton(resizedIcon);
		btnRent.setVerticalTextPosition(JButton.BOTTOM);
		btnRent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dialog=new RentReturn();
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		btnRent.setBounds(320,440,60,40);
		this.add(btnRent);
		
		
		JLabel lblReturn = new JLabel("Return");
		lblReturn.setBounds(417,410,80,20);
		lblReturn.setForeground(Color.WHITE);
		this.add(lblReturn);
		ImageIcon imgReturn = new ImageIcon("img/return.png");
		Image img2 = imgReturn.getImage(); // 이미지 가져오기
		Image resizedImg2 = img2.getScaledInstance(60, 40, java.awt.Image.SCALE_SMOOTH); // 버튼 크기에 맞게 이미지 크기 조정
		ImageIcon resizedIcon2 = new ImageIcon(resizedImg2); // 조정된 이미지로 새로운 ImageIcon 생성
		
//		ImageIcon imgReturn = new ImageIcon(getClass().getClassLoader().getResource("return.png"));
//		btnReturn=new JButton(imgReturn);
		btnReturn = new JButton(resizedIcon2);
		btnReturn.setVerticalTextPosition(JButton.BOTTOM);
		btnReturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIManager.put("OptionPane.yesButtonText", "Yes");
				UIManager.put("OptionPane.noButtonText", "No");
				UIManager.put("OptionPane.cancelButtonText", "Cancel");
				
				
				//경고문 반납하시겠습니까 Y/N Y면 실행 N이면 취소
				int confirm = JOptionPane.showConfirmDialog(null, "Would you like to return it?","Return Confirmation",JOptionPane.YES_NO_OPTION);
				if(confirm==JOptionPane.YES_OPTION) {
					Connection conn=null;
					try {
						//oracle jdbc driver load
						Class.forName("oracle.jdbc.driver.OracleDriver");
						//Connection
						conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
						
						Statement stmt=conn.createStatement(); // statement객체 생성
						Locale.setDefault(Locale.ENGLISH);

			            // UIManager를 사용해 "확인" 버튼을 "OK"로 변경
			            UIManager.put("OptionPane.okButtonText", "OK");
						
						
						//Application에서 sql실행시 기본적으로 auto commit.

						//update
						//클릭한값 읽어오기
						stmt.executeUpdate("delete from rentbook where id= '"+clickedID+"' and rdate='"+clickedDate+"'");
						stmt.executeUpdate("update book set inventory=inventory+1 where title='"+clickedTitle+"'");

						//select
						ResultSet rs=stmt.executeQuery(query);//select문 실행.
						// rs는 cursor역할. 한행씩 while문으로 fetch
						
						//목록 초기화
						model.setRowCount(0);
						while(rs.next()) {
							String[] row=new String[4];
							row[0]=rs.getString("id");
							row[1]=rs.getString("name");
							row[2]=rs.getString("title");
							row[3]=rs.getString("rdate");
							model.addRow(row);
						}
						
						JOptionPane.showConfirmDialog(null, "ID: "+clickedID+", "+clickedTitle+" Returned Successfully","Return Confirmation",JOptionPane.PLAIN_MESSAGE);
					}catch(Exception e1) {
						e1.printStackTrace();
					}finally {
						try {
							if(rs!=null) {rs.close();} 
							if(stmt!=null) {stmt.close();}
							if(con!=null) {con.close();}
						}catch(Exception e2) {
							e2.printStackTrace();
						}
					}
				} 
				
			}});
		btnReturn.setBounds(400,440,60,40);
		this.add(btnReturn);
		
		
		this.setVisible(true);
	}
	
	//select 실행 , list 메소드
	public void list() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");//oracle driver 로드
			// oracle xe연결. enterprise는 xe대신 orcl
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
			// statement객체 생성.
			stmt=con.createStatement();			
			rs=stmt.executeQuery(query);
			//목록 초기화
			model.setRowCount(0); // medel의 행의 수를 0으로 변경
			
			// fetch. 한행씩 읽어오기
			while(rs.next()) {
				String[] row=new String[4];
				row[0]=rs.getString("id");
				row[1]=rs.getString("name");
				row[2]=rs.getString("title");
				row[3]=rs.getString("rdate");
				model.addRow(row);
			}
		}catch(Exception e1) {
			e1.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}  // rs==null: 레퍼런스, 인스턴스가 없다
				if(stmt!=null) {stmt.close();}
				if(con!=null) {con.close();}
			}catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	 
	// 대출 버튼이 있는 다이얼로그
	class RentReturn extends JDialog {
		JLabel lblID1=null;
		JTextField tfID1=null;
		JLabel lblTitle1=null;
		JTextField tfTitle1=null;
		JLabel lblInventory1=null;
		JTextField tfInventory1=null;
		
		DefaultTableModel model1=null;
		JTable table1=null;
		
		public RentReturn () {
			this.setBackground(new Color(57,40,28));
			this.setTitle("Loan Return");
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setLayout(new FlowLayout());
			
			lblID1= new JLabel("Student ID");
			add(lblID1);
			tfID1=new JTextField(13);
			add(tfID1);
			lblTitle1=new JLabel("Book Title");
			add(lblTitle1);
			tfTitle1=new JTextField(13);
			add(tfTitle1);
			
			String[] colName={"Book Title","Stock Quantity",};
			model1=new DefaultTableModel(colName,0); // model:데이터
			table1=new JTable(model1);// table과 model 바인딩(binding)
			table1.setPreferredScrollableViewportSize(new Dimension (530,300)); // 가로, 세로크기
			JScrollPane tSP = new JScrollPane(table1); // 스크롤바 구현
			this.add(tSP);
			
			table1.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					table1=(JTable) e.getComponent(); // 컴포넌트를 JTabel로 변환
					model1=(DefaultTableModel) table1.getModel();
					String title=(String)model1.getValueAt(table1.getSelectedRow(), 0);
					tfTitle1.setText(title);
				}
				@Override
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
			});

			//초기 목록	
			String query = "select title, inventory from book";
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");//oracle driver 로드
				// oracle xe연결. enterprise는 xe대신 orcl
				con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
				// statement객체 생성.
				stmt=con.createStatement();			
				rs=stmt.executeQuery(query);
				
				//목록 초기화
				model1.setRowCount(0); // model의 행의 수를 0으로 변경				
				// fetch. 한행씩 읽어오기
				while(rs.next()) {
					String[] row=new String[2];
					row[0]=rs.getString("title");
					row[1]=rs.getString("inventory");
					model1.addRow(row);
				}
			}catch(Exception e1) {
				e1.printStackTrace();
			}finally {
				try {
					if(rs!=null) {rs.close();}  // rs==null: 레퍼런스, 인스턴스가 없다
					if(stmt!=null) {stmt.close();}
					if(con!=null) {con.close();}
				}catch(Exception e2) {
					e2.printStackTrace();
				}
			}

			// 대출 book: inventory-1(inventory가 0이면 안되고 경고메세지 뜨게), rentBook: 추가
			JButton btnRent=new JButton("Loan");
			this.setBackground(new Color(57,40,28));
			btnRent.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			        Connection conn=null;
			        try {
			            //oracle jdbc driver load
			            Class.forName("oracle.jdbc.driver.OracleDriver");
			            //Connection
			            conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
			            
			            Statement stmt=conn.createStatement(); // statement객체 생성
			            
			            ResultSet rs=stmt.executeQuery("select inventory from book where title= '"+tfTitle1.getText()+"'" );
			            String result="";
			            while(rs.next()) {
			                result=rs.getString("inventory");
			            }
			            
			            if(Integer.parseInt(result)<=0) {
			                UIManager.put("OptionPane.okButtonText", "OK");

			                JOptionPane.showMessageDialog(table1, "The book is out of stock and cannot be loaned.", "Out of stock",JOptionPane.WARNING_MESSAGE);
			            } else {                        
			                // 대출 시간 년.월.일 시:분:초
			                Date now=new Date();
			                SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			                //update                                                                      
			                stmt.executeUpdate("insert into rentbook values (rbook_seq_num.nextval, '"+tfID1.getText()+"', "
			                        + "(select bid from book where title='"+tfTitle1.getText()+"'), '"+f.format(now)+"')");
			                stmt.executeUpdate("update book set inventory=inventory-1 where title='"+tfTitle1.getText()+"'");

			                //select
			                ResultSet rs1=stmt.executeQuery("select * from book");//select문 실행.
			                // rs는 cursor역할. 한행씩 while문으로 fetch
			                
			                //목록 초기화
			                model1.setRowCount(0);
			                while(rs1.next()) {
			                    String[] row=new String[2];
			                    row[0]=rs1.getString("title");
			                    row[1]=rs1.getString("inventory");
			                    model1.addRow(row);
			                }
			                UIManager.put("OptionPane.okButtonText", "OK");

			                JOptionPane.showConfirmDialog(null, "ID: "+tfID1.getText()+", The Loan for "+tfTitle1.getText()+" has been processed !","Check Loan",JOptionPane.PLAIN_MESSAGE);
			            }
			            
			        }catch(Exception e1) {
			            e1.printStackTrace();
			        }finally {
			            try {
			                if(rs != null && !rs.isClosed()) rs.close();
			                if(stmt != null && !stmt.isClosed()) stmt.close();
			                if(con != null && !con.isClosed()) con.close();
			            } catch (SQLException e2) {
			                e2.printStackTrace();
			            }
			        
			        };
			        
				}});
			add(btnRent);
			
			this.setSize(600,450);
			setLocationRelativeTo(null);
		}

	}

	public static void main(String[] args) {
		new BookRent();
	}

}

