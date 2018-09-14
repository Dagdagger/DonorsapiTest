import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import javax.json.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class Fetch {
	
	 private static String ENTER = "Enter";
	    static JButton enterButton;
	    public static String searchRequest;
	    public static JTextArea output;
	    public static JTextField input;
	    static JFrame frame;
	    static JPanel panel;
	    public static String testString = "Enter one Search Criteria";
	
	    
	    /** Code derived from https://stackoverflow.com/questions/16390503/java-swing-getting-input-from-a-jtextfield */
	    
	 public static void createFrame()
	    {
	        frame = new JFrame("Enter one Search Criteria");
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        panel.setOpaque(true);
	        ButtonListener buttonListener = new ButtonListener();
	        output = new JTextArea(15, 50);
	        output.setWrapStyleWord(true);
	        output.setEditable(false);
	        JScrollPane scroller = new JScrollPane(output);
	        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	        JPanel inputpanel = new JPanel();
	        inputpanel.setLayout(new FlowLayout());
	        input = new JTextField(20);
	        enterButton = new JButton("Enter");
	        enterButton.setActionCommand(ENTER);
	        enterButton.addActionListener(buttonListener);
	        // enterButton.setEnabled(false);
	        input.setActionCommand(ENTER);
	        input.addActionListener(buttonListener);
	        DefaultCaret caret = (DefaultCaret) output.getCaret();
	        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	        panel.add(scroller);
	        inputpanel.add(input);
	        inputpanel.add(enterButton);
	        panel.add(inputpanel);
	        frame.getContentPane().add(BorderLayout.CENTER, panel);
	        frame.pack();
	        frame.setLocationByPlatform(true);
	        // Center of screen
	        // frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setResizable(false);
	        input.requestFocus();
	    }

	    public static class ButtonListener implements ActionListener
	    {
	    

	        public void actionPerformed(final ActionEvent ev)
	        {
	            if (!input.getText().trim().equals(""))
	            {
	                String cmd = ev.getActionCommand();
	                if (ENTER.equals(cmd))
	                {
	                    searchRequest = input.getText();
	                	URL url = null;
						try {
							url = new URL("https://api.donorschoose.org/common/json_feed.html?keywords="+searchRequest+"&state=CA&APIKey=DONORSCHOOSE");
						} catch (MalformedURLException e1) {
						
							e1.printStackTrace();
						}
	            		String inline = "";
						try {
							inline = search(url);
						} catch (IOException e) {
					
							e.printStackTrace();
						}
	            		JSONParser parse = new JSONParser();
	            		JSONObject jobj = null;
						try {
							jobj = (JSONObject)parse.parse(inline);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		JSONArray jsonarr_1 = (JSONArray) jobj.get("proposals");
	            		
	            		
	            		 List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	            		    for (int i = 0; i < jsonarr_1.size(); i++) {
	            		        jsonValues.add((JSONObject)jsonarr_1.get(i));
	            		    }
	            		System.out.println(jobj.get("searchTerms"));
	            		
	            		 Collections.sort(jsonValues, new Comparator<JSONObject>() {
	            		        
	            			    private static final String KEY_NAME = "povertyType";
	            			 	private static final String KEY_NAME_2 = "label";
	            		    
								@Override
								public int compare(JSONObject a, JSONObject b) {
									
									int compare = 0;
									
									try {
									
									HashMap<String, Integer> labelSorts = new HashMap<String, Integer>();
									labelSorts.put("LOW", 1);
									labelSorts.put("MODERATELOW", 2);
									labelSorts.put("MODERATEHIGH", 3);
									labelSorts.put("HIGH", 4);
									
									JSONObject a_2 = (JSONObject) a.get(KEY_NAME);
									JSONObject b_2 = (JSONObject) b.get(KEY_NAME);
									
									
									
									int valA = labelSorts.get((String)a_2.get(KEY_NAME_2));
								 	int valB = labelSorts.get((String)b_2.get(KEY_NAME_2));
								 	
								 	return Integer.compare(valA, valB);
									}
									catch (JsonException e)
									{
										e.printStackTrace();
									}
									return compare;
										
								
								}
	            		 });
	            		 
	            		 JSONArray sortedJsonArray = new JSONArray();
	            		 
	            		 for (int i = 0; i < jsonarr_1.size(); i++) {
	            			 
	      
	            			  sortedJsonArray.add(jsonValues.get(i));
	            		 }
	            			 
	            			 
	            		
	            		
	            		
	            		//Get data from Proposals array
	            		 
	            		int i = 0;
	            		int totalOut = 0;
	            		int percentFunded = 0, numOfDonors = 0, numOfStudents = 0;

	            		double costToComplete = 0.00,totalPrice = 0.00;
	            		
	    
	            		
	            		
	            		while (totalOut < 5)
	            		{
	            		JSONObject test = (JSONObject)jsonarr_1.get(i);
	            		
	            		if ((Float.parseFloat((String)test.get("totalPrice"))) > 2000) {
	            			i++;
	            			continue;
	            		}
	            		//Store the JSON objects in an array
	            		//Get the index of the JSON object and print the values as per the index
	            		
	            		JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
	            		output.append("\nTitle: " +jsonobj_1.get("title") + "\n");
	            		output.append("\nShort Description: " +jsonobj_1.get("shortDescription")+ "\n");
	            		output.append("\nCost To Complete: " +jsonobj_1.get("costToComplete")+ "\n");
	            		output.append("\nProposal URL: " +jsonobj_1.get("proposalURL")+ "\n");
	            		output.append("\nTotalPrice: " +jsonobj_1.get("totalPrice")+ "\n");
	            		percentFunded += Integer.parseInt((String)jsonobj_1.get("percentFunded"));
	            		numOfDonors += Integer.parseInt((String)jsonobj_1.get("numDonors"));
	            		numOfStudents += Integer.parseInt((String)jsonobj_1.get("numStudents"));
	            		costToComplete += Double.parseDouble((String)jsonobj_1.get("costToComplete"));
	            		totalPrice += Double.parseDouble((String)jsonobj_1.get("totalPrice"));
	            		System.out.println(costToComplete);
	            		System.out.println(totalPrice);
	            		totalOut++;
	            		i++;
	            		}
	            		System.out.println(costToComplete/5);
	            		System.out.println(totalPrice/5);
	            		output.append("\nAverage PercentFunded: " + (percentFunded/5));
	            		output.append("\nAverage Number of Donors: " + (numOfDonors/5));
	            		output.append("\nAverage Number of Students: " + (numOfStudents/5));
	            		DecimalFormat df = new DecimalFormat("#.##");
	            		output.append("\nAverage Cost to Compelete: " + df.format(costToComplete/5.00));
	            		output.append("\nAverage Total Price: " +  df.format(totalPrice/5.00));
	                }
	            }
	            input.setText("");
	            input.requestFocus();
	        }
	    }
	
	
	public static String search(URL url) throws IOException {
		
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.connect();
		int responsecode = con.getResponseCode();
		String inline = "";

		if(responsecode != 200) 
			throw new RuntimeException("HttpResponseCode: "+ responsecode);
		else
		{	

					Scanner sc = new Scanner(url.openStream());
					while(sc.hasNext())
							{
							inline+=sc.nextLine();
							inline+=("\n");
							}
					System.out.println("\n JSON data in string format");
					System.out.println(inline);
					sc.close();
		}		
		
		
		
		return inline;	
	}
	
	
	
	public static void main(String args[]) throws IOException, ParseException {	
		
		
		try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        createFrame();
  
        /*
		
		URL url = new URL("https://api.donorschoose.org/common/json_feed.html?keywords="+searchRequest+"&APIKey=DONORSCHOOSE");
		String inline = search(url);
		JSONParser parse = new JSONParser();
		JSONObject jobj = (JSONObject)parse.parse(inline);
		JSONArray jsonarr_1 = (JSONArray) jobj.get("proposals");
		System.out.println(jobj.get("searchTerms"));
		
	
		
		
		
		//Get data for Proposals array
		for(int i=0;i<jsonarr_1.size();i++)
		{
		//Store the JSON objects in an array
		//Get the index of the JSON object and print the values as per the index
		JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
		System.out.println("Elements under proposals array");
		System.out.println("\nTitle: " +jsonobj_1.get("title"));
		System.out.println("\nShort Description: " +jsonobj_1.get("shortDescription"));
		System.out.println("\nCost To Complete: " +jsonobj_1.get("costToComplete"));
		System.out.println("\nProposal URL: " +jsonobj_1.get("proposalURL"));
		System.out.println("\nTotalPrice: " +jsonobj_1.get("totalPrice"));
		}
		*/
		
		
		 
		
	}

}
