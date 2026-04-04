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

public class Races {

    //public NodeList races;
    public ArrayList<String> subRaces = new ArrayList<String>();
    public ArrayList<String> features = new ArrayList<String>();
    public ArrayList<String> languages = new ArrayList<String>();
    public Context currentContext;


    public Races(Context incomingContext){
        currentContext = incomingContext;
    }

    public ArrayList<String> getSubRaces(String race) throws Exception {
        LoadSubRaces(race);
        return subRaces;
    }

    public ArrayList<String> getFeatures(String race) throws Exception {
        LoadRaceFeatures(race);
        return features;
    }

    public ArrayList<String> getLanguages(String race) throws Exception {
        LoadRaceLanguages(race);
        return languages;
    }

    public void LoadSubRaces(String race) throws Exception {
        subRaces.clear();
        NodeList races;
        AssetManager assetManager = currentContext.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("races.xml");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        String xpathExpression = "/races/race/name[starts-with(text(), '" + race + "')]";
        //AssetFileDescriptor descriptor = currentContext.getAssets().openFd("races.xml");
        races = (NodeList) xPath.evaluate(xpathExpression, new InputSource(inputStream), XPathConstants.NODESET);
        for (int i = 0; i < races.getLength(); i++) {
            //NodeList currentRace = (NodeList) races.item(i);
            subRaces.add(races.item(i).getFirstChild().getNodeValue());
        }
    }

    public void LoadRaceFeatures(String Selectedrace) throws Exception {
        features.clear();
        NodeList race;
        AssetManager assetManager = currentContext.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("races.xml");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        String xpathExpression = "/races/race[name='" + Selectedrace + "']";
        //AssetFileDescriptor descriptor = currentContext.getAssets().openFd("races.xml");
        race = (NodeList) xPath.evaluate(xpathExpression, new InputSource(inputStream), XPathConstants.NODESET);
        Element currentRace = (Element) race.item(0);
        features.add("Size: " + xPath.evaluate("size", currentRace));
        features.add("Speed: " + xPath.evaluate("speed", currentRace));
        features.add("Ability Mods: " + xPath.evaluate("ability", currentRace));
        NodeList traits = (NodeList) xPath.evaluate("trait", currentRace, XPathConstants.NODESET);
        for (int i = 0; i < traits.getLength(); i++) {
            Element trait = (Element) traits.item(i);
            String Name = xPath.evaluate("name", trait);
            String Text = xPath.evaluate("text", trait);
            features.add(Name + ": " + Text);
        }
    }

    public void LoadRaceLanguages(String Selectedrace) throws Exception {
        languages.clear();
        NodeList race;
        AssetManager assetManager = currentContext.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("races.xml");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        String xpathExpression = "/races/race[name='" + Selectedrace + "']";
        race = (NodeList) xPath.evaluate(xpathExpression, new InputSource(inputStream), XPathConstants.NODESET);
        Element currentRace = (Element) race.item(0);
        NodeList raceLanguages = (NodeList) xPath.evaluate("language", currentRace, XPathConstants.NODESET);
        for (int i = 0; i < raceLanguages.getLength(); i++) {
            Element language = (Element) raceLanguages.item(i);
            languages.add(language.getFirstChild().getNodeValue());
        }
    }
}