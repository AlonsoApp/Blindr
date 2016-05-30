package com.cloupix.blindr.logic.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CommunicationManagerTCP {

	private Socket socket;
	private DataOutputStream dos;
	private BufferedReader br;
	private DataInputStream dis;
	private String CLRF = "\r\n";
	private String SEPARATOR = ";";

	private final int CODE_CONTINUE = 100;
    private final int CODE_OK = 200;
    private final int CODE_PARTIAL_CONTENT = 206;
    private final int CODE_NOT_FOUND = 404;
    private final int CODE_SERVER_ERROR = 500;

    public CommunicationManagerTCP(String serverTCPIP, int serverTCPPort) throws IOException {
        /*
	    socket = new Socket(serverTCPIP, serverTCPPort);
	    
	    dos = new DataOutputStream(socket.getOutputStream());
	    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    dis = new DataInputStream(socket.getInputStream());
	    */
	}

	public String sendMsg(String msg) throws IOException {
        int result = 600;

        dos.writeBytes(msg + CLRF);

        /**Iniciamos espera respuesta USER*/
        String linea = br.readLine();

        return linea;
    }

	public ArrayList<Double> getWallLossFactors(double x0, double y0, double x1, double y1) throws Exception {

        ArrayList<Double> list = new ArrayList<Double>();
        list.add(12.5);
        return list;
        //return new ArrayList<Double>(){{Double.valueOf(12.5);}};
        /*
		ArrayList<Double> list = new ArrayList<Double>();
		dos.writeBytes("WALL_LOSS_FACTORS" + CLRF);
		String line = br.readLine();
		int resultCode = Integer.parseInt(line.substring(0, 3));
		if(resultCode!=CODE_CONTINUE)
			throw new Exception("Server "+resultCode);

		dos.writeBytes(x0 + SEPARATOR + y0 + SEPARATOR + x1 + SEPARATOR + y1 + CLRF);

		boolean finish = false;

		while (!finish){
			line = br.readLine();
			resultCode = Integer.parseInt(line.substring(0, 3));
			switch (resultCode){
				case CODE_PARTIAL_CONTENT://OK
					line = br.readLine();
					StringTokenizer st = new StringTokenizer(line, SEPARATOR);
					while(st.hasMoreElements()){
						list.add(Double.parseDouble(st.nextToken().trim()));
					}
					break;
				case CODE_OK://Fin
					finish = true;
					break;
				default:
					throw new Exception("Server Error "+resultCode);
			}
		}

		return list;
		*/
	}
    
    public void desconectar() throws IOException{
    	dos.writeBytes("BYE" + CLRF);
    	dos.close();
    	br.close();
    	socket.close();
    }


}

