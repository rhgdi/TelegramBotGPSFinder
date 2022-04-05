import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    //Read constants BOT_TOKEN and BOT_NAME from a file
    public static final HashMap<String, String> keyValues = new HashMap<>();
    static {
        BufferedReader br = null;
        String line = null;
        try {
            br = new BufferedReader(new FileReader("datafile.txt"));
            while ((line=br.readLine()) !=null) {
                keyValues.put(line.split(" ")[0], line.split(" ")[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final private String BOT_TOKEN = keyValues.get("BOT_TOKEN");
    final private String BOT_NAME = keyValues.get("BOT_NAME");

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try{
            if(update.hasMessage() && update.getMessage().hasText())
            {
                Message input = update.getMessage();

                String chatId = input.getChatId().toString();

                String response = parseMessage(input.getText(), chatId);

                SendMessage output = new SendMessage();

                setButtons(output);

                output.setChatId(chatId);
                output.setText(response);

                execute(output);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String parseMessage(String textMsg, String chatId) {
//FIXME: Why this initialization
        String response = null;

        if(textMsg.equals("/start"))
            response = "Type /get, /hi";
        
        else if(textMsg.equals("/get"))
            response = "try";

//        else if(textMsg.equals("/hi")) {
//              try {
//                mLocation mLoc = new mLocation();
//                Location location = new Location();
//                Message message = new Message();
//                Double mylat = 56.00;
//                Double mylon = 37.00;
//                mLoc.setLon(message.getLocation().getLongitude());
//                mLoc.setLat(message.getLocation().getLatitude());
//                Double mylat = message.getLocation().getLongitude();
//                Double mylon = message.getLocation().getLongitude();
//                message = execute(new SendLocation(chatId,mLoc.getLon(),mLoc.getLat()));
//            }
//            catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
        
        else
            response = "Something goes wrong";

        return response;
    }
    public void setButtons( SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup (replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        KeyboardButton k = new KeyboardButton("Местоположение");
        k.setRequestLocation(true);
        keyboardFirstRow.add(k);

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
