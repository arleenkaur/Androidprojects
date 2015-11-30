/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testmaker;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.rmi.runtime.Log;

/**
 *
 * @author Sid
 */
public class XML {
    
    
     boolean saveXml(String path,ArrayList<String>questions,ArrayList<String> answers, ArrayList<String>a,ArrayList<String>b,ArrayList<String>c,ArrayList<String>d)
    {
        boolean done=false;

        try {
            done=true;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Test");
            doc.appendChild(rootElement);

            for(int i=0;i<questions.size();i++)
            {


                Element message = doc.createElement("Question");
                rootElement.appendChild(message);

                Element ques = doc.createElement("Q");
                ques.appendChild(doc.createTextNode(questions.get(i)));
                message.appendChild(ques);
                
                
                 Element ans = doc.createElement("answer");
                ans.appendChild(doc.createTextNode(answers.get(i)));
                message.appendChild(ans);

                Element aNode = doc.createElement("a");
                aNode.appendChild(doc.createTextNode(a.get(i)));
                message.appendChild(aNode);

                Element bNode = doc.createElement("b");
                bNode.appendChild(doc.createTextNode(b.get(i)));
                message.appendChild(bNode);


                Element cNode = doc.createElement("c");
                cNode.appendChild(doc.createTextNode(c.get(i)));
                message.appendChild(cNode);

                Element dNode = doc.createElement("d");
                dNode.appendChild(doc.createTextNode(d.get(i)));
                message.appendChild(dNode);

              


            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            
            
            
            File file = new File(path+"/testFile.xml");
            file.createNewFile();
            OutputStream outStream = null;

            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            System.out.println("File saved!");



        } catch (Exception pce) {
            pce.printStackTrace();
            done=false;
        }
        return  done;
    }
    
}
