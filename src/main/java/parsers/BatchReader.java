package parsers;

import utils.BufferReaderUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihangz
 */
public class BatchReader {
    private BufferedReader bufferedReader;
    public BatchReader(InputStream inputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }
    public List<String> readBatch(){
        try{
            String line = this.bufferedReader.readLine();
            if(line == null){
                return null;
            }
            List<String> commandList;
            if(line.startsWith("$")){
                //single command
                commandList = BufferReaderUtil.readNextKLines(this.bufferedReader,1);
            } else if (line.startsWith("*")) {
                //array of commands
                int num2Read = Integer.parseInt(line.substring(1));
                commandList = BufferReaderUtil.readNextKLines(this.bufferedReader,num2Read);
            }else{
                return null;
            }
            return commandList;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}