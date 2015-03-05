/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package redmonkey;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class RMLoader implements AssetLoader{
    
    public Object load(AssetInfo assetInfo) throws IOException {
        InputStream is = assetInfo.openStream();
        java.util.Scanner s = new java.util.Scanner(is,"UTF-8").useDelimiter("\\A");
        String script = s.hasNext() ? s.next() : "";
        is.close();
        return script;
    }
    
}
