import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


@SuppressWarnings("serial")
public class DpExchange extends JFrame implements ActionListener{

	private JPanel panel,contentPane;
	private JLabel jlabel_scale;
	private JTextArea jta_1,jta_2;
	private JComboBox jcb_1,jcb_2;
	
	private double scale = 1.0;/** 默认比利，240->240,1.0*/

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DpExchange frame = new DpExchange();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DpExchange() {
		setTitle("dp转换工具");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		setResizable(false);
		
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BorderLayout(0, 0));
		setContentPane(panel);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		panel.add(contentPane, BorderLayout.CENTER);
		
		JPanel jp_right = new JPanel();
		JLabel jl_right = new JLabel("Copyright © Staven.Mobile. 2010-2012");
		jl_right.setFont(new Font("微软雅黑", Font.PLAIN, 10));
		jl_right.setForeground(Color.WHITE);
		jp_right.add(jl_right, BorderLayout.CENTER);
		jp_right.setBackground(Color.BLACK);
		panel.add(jp_right, BorderLayout.SOUTH);
		
		Dimension dimen = new Dimension(180, 180);
		
		jta_1 = new JTextArea();
		
		JScrollPane panel = new JScrollPane(jta_1);
		panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.setPreferredSize(dimen);
		contentPane.add(panel, BorderLayout.WEST);
		
		jta_2 = new JTextArea();
		
		JScrollPane panel_1 = new JScrollPane(jta_2);
		panel_1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel_1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel_1.setPreferredSize(dimen);
		contentPane.add(panel_1, BorderLayout.EAST);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		JButton jb_cls_1= new JButton("↑-清空");
		jb_cls_1.setFont(new Font("微软雅黑", Font.BOLD, 20));
		jb_cls_1.setForeground(Color.GREEN);
		jb_cls_1.setActionCommand("1");
		jb_cls_1.addActionListener(this);
		panel_2.add(jb_cls_1);
		
		JButton jb_do = new JButton("转换");
		jb_do.setFont(new Font("微软雅黑", Font.BOLD, 20));
		jb_do.setForeground(Color.BLUE);
		jb_do.setActionCommand("0");
		jb_do.addActionListener(this);
		panel_2.add(jb_do);
		
		JButton jb_cls_2 = new JButton("清空-↑");
		jb_cls_2.setFont(new Font("微软雅黑", Font.BOLD, 20));
		jb_cls_2.setForeground(Color.GREEN);
		jb_cls_2.setActionCommand("2");
		jb_cls_2.addActionListener(this);
		panel_2.add(jb_cls_2);
		
		panel_2.setLayout(new GridLayout(1, 3));
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.CENTER);
		
		Font font_jrc = new Font("微软雅黑", Font.PLAIN, 15);
		Dimension dimen_jrc = new Dimension(80, 30);
		
		jcb_1 = new JComboBox();
		jcb_1.addItem("240");
		jcb_1.addItem("320");
		jcb_1.addItem("360");
		jcb_1.addItem("400");
		jcb_1.setFont(font_jrc);
		jcb_1.setForeground(Color.BLUE);
		jcb_1.addActionListener(this);
		jcb_1.setActionCommand("3");
		jcb_1.setPreferredSize(dimen_jrc);
		panel_3.add(jcb_1);
		
		JLabel jl_to = new JLabel("TO");
		jl_to.setFont(font_jrc);
		jl_to.setForeground(Color.RED);
		jl_to.setPreferredSize(dimen_jrc);
		panel_3.add(jl_to);
		
		jcb_2 = new JComboBox();
		jcb_2.addItem("240");
		jcb_2.addItem("320");
		jcb_2.addItem("360");
		jcb_2.addItem("400");
		jcb_2.setFont(font_jrc);
		jcb_2.setForeground(Color.BLUE);
		jcb_2.addActionListener(this);
		jcb_2.setActionCommand("4");
		jcb_2.setPreferredSize(dimen_jrc);
		panel_3.add(jcb_2);
		
		jlabel_scale = new JLabel("<html><center>比例<br>"+scale+"</center></html>");
		jlabel_scale.setFont(new Font("微软雅黑", Font.BOLD, 20));
		jlabel_scale.setForeground(Color.RED);
		panel_3.add(jlabel_scale);
		
		panel_3.setLayout(new FlowLayout());
		panel_3.setBorder(new TitledBorder("比例设置"));
		
	}

	public void actionPerformed(ActionEvent e) {
		int action = Integer.valueOf(e.getActionCommand());
		switch(action){
		case 0:
			String ori = jta_1.getText().toString();
			if(!ori.equals("")){
				jta_2.setText(compute(ori));
			}else{
				JOptionPane.showMessageDialog(this
						, "内容不能为空"
						, "提示"
						, JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		case 1:
			jta_1.setText("");
			break;
		case 2:
			jta_2.setText("");
			break;
		case 3:
		case 4:
			double sss = (Integer.valueOf(jcb_2.getSelectedItem().toString())+0.0) / Integer.valueOf(jcb_1.getSelectedItem().toString());
			/** 保留三位小数点，四舍五入*/
			BigDecimal bigDecimal = new BigDecimal(sss);
			sss = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			scale = sss;
			jlabel_scale.setText("<html><center>比例<br>"+scale+"</center></html>");
			break;
		}
	}
	
	/**
	 * 重新生成字符串
	 * @param str
	 * @return
	 */
	private String compute(String str){
		
		StringBuilder sb = new StringBuilder();
		
		try{
			BufferedReader br = new BufferedReader(new StringReader(str));
			String line = "";
			
			while((line = br.readLine()) != null){
				String tmpStr = "";
				if(line.contains("<dimen")){
					
					if(line.contains("dp")){
						tmpStr = line.substring(line.indexOf(">")+1, line.lastIndexOf("dp"));
						tmpStr = line.replace(tmpStr, String.valueOf(toScaleSize(tmpStr)));
					}else if(line.contains("sp")){
						tmpStr = line.substring(line.indexOf(">")+1, line.lastIndexOf("sp"));
						tmpStr = line.replace(tmpStr, String.valueOf(toScaleSize(tmpStr)));
					}
					
					sb.append(tmpStr+"\n");
					
				}else{
					sb.append(line+"\n");
				}
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	/**
	 * 计算结果
	 * 四舍五入
	 * 整数
	 * @param str
	 * @return
	 */
	private int toScaleSize(String str){
		
		BigDecimal big = new BigDecimal(
				String.valueOf(scale*Integer.valueOf(str))).setScale(0, BigDecimal.ROUND_HALF_UP);
		
		return big.intValue();
	}

}
