/*
* XML Parsing using kxml2
* Author : Naveen Balani
*/

//KXML Apis
import org.kxml2.io.*;
import org.xmlpull.v1.*;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;

import java.io.*;
import java.util.Vector;

public class XMLJ2MEService extends MIDlet implements CommandListener {

    //Form Name
    Form mainForm = new Form ("SampleJ2MEXML");
    TextField txtField = new TextField( "search:", "", 50, TextField.ANY);
    //Location of xml file
    Vector bookVector = new Vector();

    Font bigFont = Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_LARGE);
    StringItem resultItem = new StringItem ("", "");
    private final static Command xmlCommand = new Command("Get XML Data", Command.OK, 1);     
    private final static Command clearCommand = new Command("Clear", Command.BACK, 2);     
    
    class ReadXML extends Thread {
      
    StringBuffer sb = new  StringBuffer(); 	
    String url = "http://turlewicz.com:4567/edict/^sora.xml";
    private StringItem resultItem;
      public void displayResult() {
     	    //Display parsed  XML file
     	    for(int i= 0 ; i< bookVector.size() ;i++){
     	    	Book book = (Book) bookVector.elementAt(i);
     	    	sb.append("\n");
     	    	sb.append(book.getName());
     	    	sb.append("\n");
     	    	sb.append(book.getDescription());
     	    	sb.append("\n--\n");
     	      }	
     	      resultItem.setLabel("Results:");
     	      resultItem.setText(sb.toString());
       }
      public void run() {
        try {
          resultItem.setText(""); 
          //Open http connection
                HttpConnection httpConnection = (HttpConnection) Connector.open(url);
          bookVector.removeAllElements();  //clear vector with books 
          //Initilialize XML parser
          KXmlParser parser = new KXmlParser();
          parser.setInput(new InputStreamReader(httpConnection.openInputStream(),"UTF-8"));
          parser.nextTag();
          parser.require(XmlPullParser.START_TAG, null, "catalog");
          //Iterate through our XML file
          while (parser.nextTag () != XmlPullParser.END_TAG)
                   readXMLData(parser);
          parser.require(XmlPullParser.END_TAG, null, "catalog");
          parser.next();
          parser.require(XmlPullParser.END_DOCUMENT, null, null);
          displayResult();
        }
         catch (Exception e) {
                e.printStackTrace ();
              resultItem.setLabel ("Error:");
              resultItem.setText (e.toString ());

        }
      }
      public void setUrl(String u){
        url = u;
      }
      public void setResultItem(StringItem ri){
        resultItem = ri; 	
      }
    }
    
    public XMLJ2MEService () {
        resultItem.setFont(bigFont);
        mainForm.append (txtField);
       	mainForm.append (resultItem);
    	  mainForm.addCommand (xmlCommand);
        mainForm.addCommand (clearCommand);
	      mainForm.setCommandListener (this);
     }
      
    public void startApp () {
	    Display.getDisplay (this).setCurrent (mainForm);
    }

    public void pauseApp () {
    }

    public void destroyApp (boolean unconditional) {
    }  

   public void commandAction(Command c, Displayable d) {
     
     
     if (c == xmlCommand) {
	        ReadXML readXML = new ReadXML();
          readXML.setResultItem(resultItem);
          readXML.setUrl("http://turlewicz.com:4567/edict/"+txtField.getString()+".xml");
          readXML.start();
	
    }
    if (c == clearCommand) {
       resultItem.setText(""); 
	  }
   }
    private void readXMLData(KXmlParser parser)
			throws IOException, XmlPullParserException {

			//Parse our XML file
			parser.require(XmlPullParser.START_TAG, null, "title");
			Book book = new Book();

			while (parser.nextTag() != XmlPullParser.END_TAG) {
				parser.require(XmlPullParser.START_TAG, null, null);
				String name = parser.getName();
				String text = parser.nextText();
				if (name.equals("name"))
					book.setName(text);
				else if (name.equals("description"))
					book.setDescription(text);
				else if (name.equals("author"))
					book.setAuthor(text);
				else if (name.equals("rating"))
					book.setRating(text);
				else if (name.equals("available"))
					book.setAvailable(text);
											
			parser.require(XmlPullParser.END_TAG, null, name);
			}
			
			bookVector.addElement(book);
			
			parser.require(XmlPullParser.END_TAG, null, "title");
		}
	}



