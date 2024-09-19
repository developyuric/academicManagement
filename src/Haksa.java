import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// 커스텀 에러 다이얼로그 클래스
class CustomErrorDialog extends JDialog {
    public CustomErrorDialog(JFrame parent, String message) {
        super(parent, "Login failed", true);
        setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout());
        messagePanel.setOpaque(false);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(Color.red);
        messagePanel.add(messageLabel);

        JButton okButton = new JButton("  Confirm  ");
        okButton.setBackground(Color.WHITE);
        okButton.setForeground(Color.BLACK);
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        messagePanel.add(okButton);

        // Set background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("img/haksa4.jpg"); // Path to your background image
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(messagePanel, BorderLayout.CENTER);

        add(backgroundPanel, BorderLayout.CENTER);

        setSize(300, 150);
        setLocationRelativeTo(parent);
        // Move dialog slightly up from the center
        Point center = new Point(Toolkit.getDefaultToolkit().getScreenSize().width / 2, 
                                Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        this.setLocation(center.x - this.getWidth() / 2, center.y - this.getHeight() / 2 - 120);
    }
}

// 로그인 다이얼로그 클래스
class MyDialog extends JDialog {
    JLabel lblID;
    JTextField tfID;
    JLabel lblPW;
    JPasswordField tfPW;
    JButton btnLogin;
    JButton btnExit;
    Connection conn;

    public MyDialog(JFrame frame, String title) {
        super(frame, title, true);
        this.setLayout(new BorderLayout());

        // Background panel with image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("img/haksa5.jpg"); // Path to your background image
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblID = new JLabel("ID");
        lblID.setForeground(Color.WHITE); // Set label text color to white
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(lblID, gbc);

        tfID = new JTextField(15);
        gbc.gridx = 1;
        backgroundPanel.add(tfID, gbc);

        lblPW = new JLabel("Password");
        lblPW.setForeground(Color.WHITE); // Set label text color to white
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(lblPW, gbc);

        tfPW = new JPasswordField(15);
        gbc.gridx = 1;
        backgroundPanel.add(tfPW, gbc);

        // Buttons panel to align buttons horizontally
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setOpaque(false);

        btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.WHITE); // Black color
        btnLogin.setForeground(Color.BLACK); // White text color
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        buttonsPanel.add(btnLogin);

        btnExit = new JButton("Exit");
        btnExit.setBackground(Color.WHITE); // Black color
        btnExit.setForeground(Color.BLACK); // White text color
        btnExit.setFocusPainted(false);
        btnExit.setBorderPainted(false);
        buttonsPanel.add(btnExit);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(buttonsPanel, gbc);

        this.add(backgroundPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Oracle JDBC driver load
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    // Connection
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "c##ora_user", "hong");

                    Statement stmt = conn.createStatement(); // Statement 객체 생성

                    // Select
                    ResultSet rs = stmt.executeQuery("select * from login"); // Select 문 실행.

                    // 목록 초기화
                    HashMap<String, String> map2 = new HashMap<>();
                    while (rs.next()) {
                        map2.put(rs.getString("id"), rs.getString("pw"));
                    }

                    // ID, PW 확인
                    if (map2.containsKey(tfID.getText()) && map2.get(tfID.getText()).equals(new String(tfPW.getPassword()))) {
                        dispose();
                    } else {
                        new CustomErrorDialog((JFrame) MyDialog.this.getOwner(), "Incorrect username or password.").setVisible(true);
                    }

                    rs.close();
                    stmt.close();
                    conn.close(); // 연결 해제
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Close the entire application
            }
        });

        // Add WindowListener to handle closing
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // Close the entire application
            }
        });

        this.setSize(300, 180);
        setLocationRelativeTo(null); // Center the dialog
        // Move dialog slightly up from the center
        Point center = new Point(Toolkit.getDefaultToolkit().getScreenSize().width / 2, 
                                Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        this.setLocation(center.x - this.getWidth() / 2, center.y - this.getHeight() / 2 - 140);
    }
}

// 메인 프레임 클래스
public class Haksa extends JFrame {
    JPanel panel; // 메뉴별 화면이 출력되는 패널
    MyDialog dialog = null;

    public Haksa() {
        this.setTitle("Academic Management");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menu Bar
        JMenuBar bar = new JMenuBar();
        // Menu
        JMenu mStudent = new JMenu("Student Management"); // 메뉴 생성
        mStudent.setPreferredSize(new Dimension(140,25));
        bar.add(mStudent); // 메뉴바에 추가
        JMenu mBookRent = new JMenu("Book Management");
        mBookRent.setPreferredSize(new Dimension(140,25));
        bar.add(mBookRent);

        // Menu Item (서브메뉴)
        JMenuItem miStudent = new JMenuItem("Student Information");
        miStudent.setPreferredSize(new Dimension(140,25));
        miStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.removeAll(); // 모든 컴포넌트 삭제
                panel.revalidate(); // 다시 활성화
                panel.repaint(); // 다시 그리기
                panel.add(new Student()); // 화면 생성
                panel.setLayout(null); // 레이아웃 적용 안 함
            }
        });
        mStudent.add(miStudent); // 학생정보 menu에 학생정보 item 추가

        JMenuItem miBookRent = new JMenuItem("Loan list");
        miBookRent.setPreferredSize(new Dimension(140, 25)); 
        miBookRent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                panel.revalidate();
                panel.repaint();
                panel.add(new BookRent());
                panel.setLayout(null);
            }
        });
        mBookRent.add(miBookRent);

        this.setJMenuBar(bar); // menu bar를 frame에 추가
//        miBookRent.setBounds(200, 10, 100, 20);
        
        // panel 생성, 추가
        this.panel = new JPanel();
        this.panel.setBackground(new Color(57,40,28));
        this.add(panel);

        ImageIcon icon = new ImageIcon("img/haksa1.jpg");
        Image img = icon.getImage();
        Image fixImg = img.getScaledInstance(800, 600, img.SCALE_SMOOTH);
        ImageIcon fixIcon = new ImageIcon(fixImg);
        JLabel lbl = new JLabel(fixIcon);
        panel.add(lbl);

        this.setSize(800, 600);
        this.setVisible(true);
        setLocationRelativeTo(null);

        // 로그인 다이얼로그 띄우기
        dialog = new MyDialog(this, "Login");
        dialog.setModal(true); // modal dialog로 설정
        dialog.setVisible(true); // dialog 보이게
    }

    public static void main(String[] args) {
        new Haksa();
    }
}
//Java, JDBC, Swing, Oracle