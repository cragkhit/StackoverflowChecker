package data;

import data.SimianLog;
import exception.SaxTerminatorException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiusWhite on 21/03/2016.
 */
public class UsefulSimianFragmentHandler extends DefaultHandler {
    public List<SimianLog> simianLogs;

    public UsefulSimianFragmentHandler() {
        this.simianLogs = new ArrayList<>();
    }

    public UsefulSimianFragmentHandler(List<SimianLog> simianLogs) {
        this.simianLogs = simianLogs;
    }


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.createSimianLog(qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (CommonUtils.TEST_MODE) {
            if (exec.IndvCloneFilter.NUMBER_OF_POSTS >= CommonUtils.STOP_PROCESS_FLAG) {
                throw new SaxTerminatorException();
            }
        }
    }
    /*
    private void getFragmentStats(String qName, Attributes attributes) {
        data.SimianStackoverflowFragment actualFragment = new data.SimianStackoverflowFragment();
        if (qName.equals("FRAGMENT_LOG")) {
            String filePath = attributes.getValue("filePath");
            if (filePath.contains("extracted_data_full")) {
                String[] parts = filePath.split("/");
                String fragmentName = parts[parts.length - 1];
                boolean exixt = false;
                for (data.SimianStackoverflowFragment fragment : this.stackoverflowFragments) {
                    if (fragment.fragmentName.equals(fragmentName)) {
                        exixt = true;
                        actualFragment = fragment;
                        break;
                    }
                }
                if (!exixt) {
                    actualFragment.fragmentName = fragmentName;
                    actualFragment.numberOfTimeIsUsed++;
                    this.stackoverflowFragments.add(actualFragment);
                }
                actualFragment.numberOfTimeIsUsed++;
            }
        }
    }
    */

    private void createSimianLog(String qName, Attributes attributes) {
        if (qName.equals("SIMIAN_LOG")) {
            this.simianLogs.add(new SimianLog());
            this.simianLogs.get(this.simianLogs.size()-1).id = Integer.parseInt(attributes.getValue("ID"));
        } else if (qName.equals("FRAGMENT_LOG")) {
            SimianLog actualSimianLog = this.simianLogs.get(this.simianLogs.size()-1);
            SimianLog.LogFragment logFragment = actualSimianLog.getEmptyFragment();
            String filePath = attributes.getValue("filePath");
            String[] parts = filePath.split("/");
            if(filePath.contains(CommonUtils.stackoverflow_path)){
                logFragment.filePath = parts[parts.length - 1];
            } else {
                // logFragment.filePath = parts[5];
                logFragment.filePath = filePath;
            }
            logFragment.isStackoverflowFragment = filePath.contains(CommonUtils.stackoverflow_path);
            logFragment.start = Integer.parseInt(attributes.getValue("start"));
            logFragment.end = Integer.parseInt(attributes.getValue("end"));
        }
    }
}
