package hendee.dndcharactermanager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Greg on 9/19/2017.
 */

public class Feats {

    //public NodeList races;
    public ArrayList<String> feats = new ArrayList<String>();
    public ArrayList<String> features = new ArrayList<String>();
    public ArrayList<String> modifiers = new ArrayList<String>();
    public Context currentContext;


    public Feats(Context incomingContext){
        currentContext = incomingContext;
    }

    public ArrayList<String> getFeats() throws Exception
    {
        LoadFeats();
        return feats;
    }
    public ArrayList<String> getFeatures(String feature) throws Exception {
        LoadFeatFeatures(feature);
        return features;
    }


    public void LoadFeats() throws Exception {
        feats.clear();
        NodeList featsList;
        AssetManager assetManager = currentContext.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("feats.xml");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        String xpathExpression = "/features/feat";
        //AssetFileDescriptor descriptor = currentContext.getAssets().openFd("races.xml");
        featsList = (NodeList) xPath.evaluate(xpathExpression, new InputSource(inputStream), XPathConstants.NODESET);
        for (int i = 0; i < featsList.getLength(); i++) {
            Element currentFeature = (Element) featsList.item(i);
            String Name = xPath.evaluate("name", currentFeature);
            features.add(Name);
        }
    }

    public void LoadFeatFeatures(String Selectedfeat) throws Exception {
        features.clear();
        NodeList feat;
        AssetManager assetManager = currentContext.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("feats.xml");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        String xpathExpression = "/feats/feat[name='" + Selectedfeat + "']";
        //AssetFileDescriptor descriptor = currentContext.getAssets().openFd("races.xml");
        feat = (NodeList) xPath.evaluate(xpathExpression, new InputSource(inputStream), XPathConstants.NODESET);
        Element currentFeat = (Element) feat.item(0);
        NodeList traits = (NodeList) xPath.evaluate("text", currentFeat, XPathConstants.NODESET);
        for (int i = 0; i < traits.getLength(); i++) {
            Element trait = (Element) traits.item(i);
            features.add(trait.getFirstChild().getNodeValue());
        }
    }
}