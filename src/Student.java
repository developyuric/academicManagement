import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class Student extends JPanel{
	JLabel lblID=null; // lbl: Label
	JTextField tfID=null; // tf: TextField
	JLabel lblName=null; 
	JTextField tfName=null;
	JLabel lblDept=null; 
	JTextField tfDept=null;
	JLabel lblAddress=null;
	JTextField tfAddress=null;
	
	JButton btnInsert = null;
	JButton btnSelect = null;
	JButton btnUpdate = null;
	JButton btnDelete = null;
	JButton btnSearch = null;
	
	DefaultTableModel model=null;
	JTable table=null;
	
	public Student() {
		// Layout default: FlowLayout
		
		this.setLayout(null);
		
        this.setBackground(new Color(57,40,28)); 
        this.
		
		lblID= new JLabel("Student ID");
        lblID.setForeground(Color.WHITE);
		add(lblID);
		lblID.setBounds(10,10,100,20);
		tfID= new JTextField(13); // 가로크기 지정
		
		add(tfID);
		tfID.setBounds(100,10,150,20);
		btnSearch= new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Connection conn=null;
				try {
					//oracle jdbc driver load
					Class.forName("oracle.jdbc.driver.OracleDriver");
					//Connection
					conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
					
					Statement stmt=conn.createStatement(); // statement객체 생성

					//select
					ResultSet rs=stmt.executeQuery("select * from student where id='"+tfID.getText()+"'");//select문 실행.
					// rs는 cursor역할. 한행씩 while문으로 fetch
					
					//목록 초기화
					model.setRowCount(0); // model의 행의 수를 0으로 설정
					
					while(rs.next()) {
						String[] row=new String[4];
						row[0]=rs.getString("id");
						row[1]=rs.getString("name");
						row[2]=rs.getString("dept");
						row[3]=rs.getString("address");
						model.addRow(row);
						
						tfID.setText(rs.getString("id"));
						tfName.setText(rs.getString("name"));
						tfDept.setText(rs.getString("dept"));
						tfAddress.setText(rs.getString("address"));
					}
						
					rs.close();
					stmt.close();
					conn.close();//연결해제
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}});
		add(btnSearch);
		btnSearch.setBounds(270,10,100,20);
		lblName= new JLabel("Name");
		lblName.setForeground(Color.WHITE);
		add(lblName);
		lblName.setBounds(10,40,100,20);
		tfName= new JTextField(20);
		add(tfName);
		tfName.setBounds(100,40,150,20);
		lblDept= new JLabel("Department");
		lblDept.setForeground(Color.WHITE);
		add(lblDept);
		lblDept.setBounds(10,70,100,20);
		tfDept= new JTextField(20);
		add(tfDept);
		tfDept.setBounds(100,70,150,20);
		lblAddress= new JLabel("Address");
		lblAddress.setForeground(Color.WHITE);
		add(lblAddress);
		lblAddress.setBounds(10,100,100,20);
		tfAddress= new JTextField(20);
		add(tfAddress);
		tfAddress.setBounds(100,100,150,20);
		
		String[] colName={"Student ID","Name","Department","Address"};
		model=new DefaultTableModel(colName,0); // model:데이터
		table=new JTable(model);// table과 model 바인딩(binding)
		table.setPreferredScrollableViewportSize(new Dimension (720,330)); // 가로, 세로크기
		JScrollPane tSP = new JScrollPane(table); // 스크롤바 구현
		this.add(tSP);
		tSP.setBounds(20,130,750,330);
		
		// 테이블을 클릭했을 때 해당 row 값들을 입력창에 출력
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				table=(JTable) e.getComponent(); // 컴포넌트를 JTabel로 변환
				model=(DefaultTableModel) table.getModel();
				String id=(String)model.getValueAt(table.getSelectedRow(), 0); // 선택한 행의 id구하기, 열 위치
				tfID.setText(id);
				String name=(String)model.getValueAt(table.getSelectedRow(), 1);
				tfName.setText(name);
				String dept=(String)model.getValueAt(table.getSelectedRow(), 2);
				tfDept.setText(dept);
				String address=(String)model.getValueAt(table.getSelectedRow(), 3);
				tfAddress.setText(address);
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		btnInsert=new JButton("Register");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection conn=null;
				try {
					//oracle jdbc driver load
					Class.forName("oracle.jdbc.driver.OracleDriver");
					//Connection
					conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
					
					Statement stmt=conn.createStatement(); // statement객체 생성
					
					//Application에서 sql실행시 기본적으로 auto commit.
					
					//insert
					stmt.executeUpdate("insert into student values('"+tfID.getText()+"','"+tfName.getText()+"','"+tfDept.getText()+"','"+tfAddress.getText()+"')"); 
					
					//select
					ResultSet rs=stmt.executeQuery("select * from student order by id");//select문 실행.
					// rs는 cursor역할. 한행씩 while문으로 fetch
					
					//목록 초기화
					model.setRowCount(0);
					while(rs.next()) {
						String[] row=new String[4];
						row[0]=rs.getString("id");
						row[1]=rs.getString("name");
						row[2]=rs.getString("dept");
						row[3]=rs.getString("address");
						model.addRow(row);
					}
					
					rs.close();
					stmt.close();
					conn.close();//연결해제
				}catch(Exception e1) {
					e1.printStackTrace();
				}
				
			}});
		add(btnInsert);
		btnInsert.setBounds(270,470,100,40);
		
		btnSelect=new JButton("List");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection conn=null;
				try {
					//oracle jdbc driver load
					Class.forName("oracle.jdbc.driver.OracleDriver");
					//Connection
					conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
					
					Statement stmt=conn.createStatement(); // statement객체 생성

					//select
					ResultSet rs=stmt.executeQuery("select * from student order by id");//select문 실행.
					// rs는 cursor역할. 한행씩 while문으로 fetch
					
					//목록 초기화
					model.setRowCount(0);
					while(rs.next()) {
						String[] row=new String[4];
						row[0]=rs.getString("id");
						row[1]=rs.getString("name");
						row[2]=rs.getString("dept");
						row[3]=rs.getString("address");
						model.addRow(row);
					}
					
					rs.close();
					stmt.close();
					conn.close();//연결해제
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		add(btnSelect);
		btnSelect.setBounds(150,470,100,40);
		
		btnUpdate=new JButton("Edit");
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Connection conn=null;
				try {
					//oracle jdbc driver load
					Class.forName("oracle.jdbc.driver.OracleDriver");
					//Connection
					conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
					
					Statement stmt=conn.createStatement(); // statement객체 생성
					
					//Application에서 sql실행시 기본적으로 auto commit.

					//update
					stmt.executeUpdate("update student set name='"+tfName.getText()+"', dept='"+tfDept.getText()+"', address='"+tfAddress.getText()+"' where id='"+tfID.getText()+"'");

					//select
					ResultSet rs=stmt.executeQuery("select * from student order by id");//select문 실행.
					// rs는 cursor역할. 한행씩 while문으로 fetch
					
					//목록 초기화
					model.setRowCount(0);
					while(rs.next()) {
						String[] row=new String[4];
						row[0]=rs.getString("id");
						row[1]=rs.getString("name");
						row[2]=rs.getString("dept");
						row[3]=rs.getString("address");
						model.addRow(row);
					}
					
					rs.close();
					stmt.close();
					conn.close();//연결해제
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}});
		add(btnUpdate);
		btnUpdate.setBounds(390,470,100,40);
		
		btnDelete=new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Connection conn=null;
				try {
					//oracle jdbc driver load
					Class.forName("oracle.jdbc.driver.OracleDriver");
					//Connection
					conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##ora_user","hong");
					
					Statement stmt=conn.createStatement(); // statement객체 생성
					
					ResultSet rs=stmt.executeQuery("select id from rentbook where id= '"+tfID.getText()+"'" );

					if(rs.next()) {
						 UIManager.put("OptionPane.okButtonText", "OK");
						
						JOptionPane.showMessageDialog(table, "Information for individuals with loan records cannot be deleted.", "Holds loan records",JOptionPane.WARNING_MESSAGE);
						rs.close();
						stmt.close();
						conn.close();
		
					} else {
						
						UIManager.put("OptionPane.yesButtonText", "Yes");
						UIManager.put("OptionPane.noButtonText", "No");
						UIManager.put("OptionPane.cancelButtonText", "Cancel");
						
						int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this?","Delete",JOptionPane.YES_NO_OPTION);
						// 결과값이 정수로 나옴, yes:0, no:1
						
						if (result==JOptionPane.YES_OPTION) {
							try {	
								Statement stmt1=conn.createStatement();
								
								//delete
								stmt1.executeUpdate("delete from student where id='"+tfID.getText()+"'");
								
								//select
								ResultSet rs1=stmt1.executeQuery("select * from student order by id");//select문 실행.
								// rs는 cursor역할. 한행씩 while문으로 fetch
								
								//목록 초기화
								model.setRowCount(0);
								while(rs1.next()) {
									String[] row=new String[4];
									row[0]=rs1.getString("id");
									row[1]=rs1.getString("name");
									row[2]=rs1.getString("dept");
									row[3]=rs1.getString("address");
									model.addRow(row);
								}
								
								//입력항목 초기화
								tfID.setText(null);
								tfName.setText(null);
								tfDept.setText(null);
								tfAddress.setText(null);
								
								rs.close();
								stmt.close();
								rs1.close();
								stmt1.close();
								conn.close();
							} catch(Exception e1) {
								e1.printStackTrace();
							}			
						}
						
					}
				} catch(Exception e1) {
					e1.printStackTrace();
				}
								
			}});
		add(btnDelete);
		btnDelete.setBounds(510,470,100,40);
		
		this.setSize(790,540);
		this.setVisible(true);
	}

}