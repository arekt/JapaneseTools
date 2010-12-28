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
    Vector wordVector = new Vector();

    Font bigFont = Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_LARGE);
    List resultList = new List("List of results", List.IMPLICIT);;
    private final static Command xmlCommand = new Command("Get XML Data", Command.OK, 1);     
    private final static Command clearCommand = new Command("Clear", Command.BACK, 2);     
    private final static Command selectItem = new Command("Select", Command.ITEM, 3);     
    
    class ReadXML extends Thread {
      
    StringBuffer sb = new  StringBuffer(); 	
    String url = "http://turlewicz.com:4567/edict/^sora.xml";
      public void displayResult() {
     	    //Display parsed  XML file
     	    for(int i= 0 ; i< wordVector.size() ;i++){
     	    	Word word = (Word) wordVector.elementAt(i);
            StringItem si = new StringItem("",word.getName()+'\n');
     	    	mainForm.append(si);
     	      }	
       }
      public void run() {
        try {
          //Open http connection
                HttpConnection httpConnection = (HttpConnection) Connector.open(url);
          wordVector.removeAllElements();  //clear vector with words 
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

        }
      }
      public void setUrl(String u){
        url = u;
      }
    }
    
    public XMLJ2MEService () {
        //resultList.setFont(bigFont); check if List got this kind of method
        mainForm.append (txtField);
    	  mainForm.addCommand (xmlCommand);
        mainForm.addCommand (clearCommand);
        mainForm.addCommand (selectItem);
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
          readXML.setUrl("http://turlewicz.com:4567/edict/"+txtField.getString()+".xml");
          readXML.start();
	
    }
    if (c == clearCommand) {
       mainForm.deleteAll();
       mainForm.append(txtField); 
	  }
    if (c == selectItem) {
      System.out.println("selectedItem:"+c);
    }
   }
    private void readXMLData(KXmlParser parser)
			throws IOException, XmlPullParserException {

			//Parse our XML file
			parser.require(XmlPullParser.START_TAG, null, "title");
			Word word = new Word();

			while (parser.nextTag() != XmlPullParser.END_TAG) {
				parser.require(XmlPullParser.START_TAG, null, null);
				String name = parser.getName();
				String text = parser.nextText();
				if (name.equals("name"))
					word.setName(text);
				else if (name.equals("description"))
					word.setDescription(text);
											
			parser.require(XmlPullParser.END_TAG, null, name);
			}
			
			wordVector.addElement(word);
			
			parser.require(XmlPullParser.END_TAG, null, "title");
		}
	}
