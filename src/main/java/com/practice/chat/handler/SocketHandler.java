package com.practice.chat.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {
	
	HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
	private static final String FILE_UPLOAD_PATH = "C:/test/websocket/";
	static int fileUploadIdx = 0;
	static String fileUploadSession = "";
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		// 메시지 발송
		String msg = message.getPayload();
		for (String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			try {
				wss.sendMessage(new TextMessage(msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//소켓 연결
		super.afterConnectionEstablished(session);
		sessionMap.put(session.getId(), session);
		
		JSONObject obj = new JSONObject();
		obj.put("type", "getId");
		obj.put("sessionId", session.getId());
		session.sendMessage(new TextMessage(obj.toJSONString()));
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//소켓 종료
		sessionMap.remove(session.getId());
		super.afterConnectionClosed(session, status);
	}
	
	private static JSONObject jsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	@Override
	public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		// 바이너리 메시지 발송
		ByteBuffer byteBuffer = message.getPayload();
		String fileName = "temp.jpg";
		File dir = new File(FILE_UPLOAD_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(FILE_UPLOAD_PATH, fileName);
		FileOutputStream out = null;
		FileChannel outChannel = null;
		try {
			byteBuffer.flip(); // byteBuffer를 읽기 위해 세팅
			out = new FileOutputStream(file, true); // 생성을 위해 OutputStream을 연다.
			outChannel = out.getChannel(); // 채널을 열고
			byteBuffer.compact(); // 파일을 복사한다.
			outChannel.write(byteBuffer); // 파일을 쓴다.
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (outChannel != null) {
					outChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		byteBuffer.position(0); // 파일을 저장하면서 position값이 변경되었으므로 0으로 초기화한다.
		// 파일쓰기가 끝나면 이미지를 발송한다.
		for (String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			try {
				wss.sendMessage((new BinaryMessage(byteBuffer)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}