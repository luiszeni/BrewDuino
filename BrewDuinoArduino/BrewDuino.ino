#include <OneWire.h>
#include <SPI.h>
#include <Ethernet.h>

// MAC address from Ethernet shield sticker under board
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(192, 168, 0, 25); // IP address, may need to change depending on network
EthernetServer server(80);  // create a server at port 80


String HTTP_req;          // stores the HTTP request

int TEMP_LAV_PIN = 9;
int TEMP_CALD_PIN = 8;
int TEMP_MOST_PIN = 7;

OneWire sondaMost(TEMP_MOST_PIN); 
OneWire sondaCald(TEMP_CALD_PIN); 
OneWire sondaLav(TEMP_LAV_PIN);

int REL_LAV_PIN = 6;
int REL_CALD_PIN = 4;
int REL_BOMBA_PIN = 5;

boolean relayLav = 0;
boolean relayCald = 0;
boolean relayBomb = 0;

boolean relayLavON = 0;
boolean relayBombON = 0;
boolean relayCaldON = 0;

float tempMost = 0;
float tempCald = 0;
float tempLav = 0;

float tempThresholdMost = 0.5;
float tempThresholdLav = 0.5;

float settedMostTemp = 29;
float settedLavTemp = 0;

void setup()
{
    Ethernet.begin(mac, ip);  // initialize Ethernet device
    server.begin();           // start to listen for clients
    Serial.begin(9600);       // for diagnostics
    pinMode(REL_LAV_PIN, OUTPUT);       // LED on pin 2
    pinMode(REL_BOMBA_PIN, OUTPUT);  
    pinMode(REL_CALD_PIN, OUTPUT);  
}

void loop()
{
    EthernetClient client = server.available();  // try to get client
    readTemps();
    processTemps();
    processRelays();
    if (client) {  // got client?
        boolean currentLineIsBlank = true;
        while (client.connected()) {
            if (client.available()) {   // client data available to read
                char c = client.read(); // read 1 byte (character) from client
                HTTP_req += c;  // save the HTTP request 1 char at a time
                // last line of client request is blank and ends with \n
                // respond to client only after last line received
                if (c == '\n' && currentLineIsBlank) {
                    // send a standard http response header
                    client.println("HTTP/1.1 200 OK");
                    client.println("Content-Type: text/html");
                    client.println("Connection: close");
                    client.println();
                    // send web page
                    
                   ProcessCheckbox(client); 
                    
                    
                    
                    ProcessThermometers(client);
               
                    //Serial.print(HTTP_req);
                    HTTP_req = "";    // finished with request, empty string
                    break;
                }
                // every line of text received from the client ends with \r\n
                if (c == '\n') {
                    // last character on line of received text
                    // starting new line with next character read
                    currentLineIsBlank = true;
                } 
                else if (c != '\r') {
                    // a text character was received from client
                    currentLineIsBlank = false;
                }
            } // end if (client.available())
        } // end while (client.connected())
        delay(1);      // give the web browser time to receive the data
        client.stop(); // close the connection
    } // end if (client)
}

void readTemps(){
 tempMost = getTemp(sondaMost);
 tempCald = getTemp(sondaCald);
 tempLav = getTemp(sondaLav);
}

void processTemps(){
  
  if(tempMost != -1000.00){
  
    if(relayCaldON){
      
      if(relayCald == 1 && tempMost >  (settedMostTemp + tempThresholdMost)){
           
           digitalWrite(REL_CALD_PIN, LOW);
            relayCald = 0;
            Serial.print("Relay Cald OFF");           
         
      }else if(relayCald == 0 && tempMost < (settedMostTemp - tempThresholdMost)){
           digitalWrite(REL_CALD_PIN, HIGH);
            relayCald = 1;
            Serial.print("Relay Cald ON  "); 
           
      }
    }else if(relayCald == 1){
           digitalWrite(REL_CALD_PIN, LOW);
            relayCald = 0;
            Serial.println("Relay Cald OFF"); 
    }
  }
  
  if(tempLav != -1000.00){
  
    if(relayLavON){
      
      if(relayLav == 1 && tempLav >  (settedLavTemp + tempThresholdLav)){
           
           digitalWrite(REL_LAV_PIN, LOW);
            relayLav = 0;
            Serial.print("Relay Lav OFF");           
         
      }else if(relayLav == 0 && tempLav < (settedLavTemp - tempThresholdLav)){
           digitalWrite(REL_LAV_PIN, HIGH);
            relayLav = 1;
            Serial.print("Relay Lav ON  "); 
           
      }
    }else if(relayLav == 1){
           digitalWrite(REL_LAV_PIN, LOW);
            relayLav = 0;
            Serial.println("Relay Lav OFF"); 
    }
  }
  
  
  if(relayBombON){
         
       if(relayBomb == 0){
         digitalWrite(REL_BOMBA_PIN, HIGH);
          relayBomb = 1;
          Serial.println("Relay bomb ON"); 
    }
  }else{
     if(relayBomb == 1){
         digitalWrite(REL_BOMBA_PIN, LOW);
          relayBomb = 0;
          Serial.println("Relay bomb OFF"); 
       }
  }
  
  
  /*
  if(relayCald){
    digitalWrite(REL_CALD_PIN, HIGH);
    
  }else{
   
    Serial.println("Relay Cald OFF");
  }
 
  if(relayLavON){
    if((tempLav + tempThresholdLav) < settedLavTemp){
          relayLav = 1;
    }else{
          relayLav = 0;  
    }
  }else{
    relayLav = 0;
  } */
}

void processRelays(){
 /* if(relayCald){
    digitalWrite(REL_CALD_PIN, HIGH);
    Serial.println("Relay Cald ON");
  }else{
    digitalWrite(REL_CALD_PIN, LOW);
    Serial.println("Relay Cald OFF");
  } 
   if(relayLav){
    digitalWrite(REL_LAV_PIN, HIGH);
    //Serial.println("Relay lav ON");
   }else{
    digitalWrite(REL_LAV_PIN, LOW);
    //Serial.println("Relay lav OFF");
   }
  if(relayBombON){
    digitalWrite(REL_BOMBA_PIN, HIGH);
    //Serial.println("Relay bomba ON");
  }else{
    digitalWrite(REL_BOMBA_PIN, LOW);    
    //Serial.println("Relay bomba OFF");
  }*/
}



void ProcessThermometers(EthernetClient cl){
   // cl.println("---Status Reles---"); 
   // cl.println("<br>");
   // cl.print("Resistencia Lavagem:");
    cl.print("<relayLav>");
    cl.print(relayLav);
    cl.print("</relayLav>");
   // cl.print("<br>");
    
   // cl.print("Resistencia Lavagem Ligada:");
    cl.print("<relayLavON>");
    cl.print(relayLavON);
    cl.print("</relayLavON>");  
   // cl.print("<br>");
    
    //cl.print("Resistencia Caldeira:");
    cl.print("<relayCald>");
    cl.print(relayCald);
    cl.print("</relayCald>");
    //cl.print("<br>");
    
    //cl.print("Resistencia Caldeira Ligada:");
    cl.print("<relayCaldON>");
    cl.print(relayCaldON);
    cl.print("</relayCaldON>");
    //cl.print("<br>");
    
    //cl.print("Bomba Ligada:");
    cl.print("<relayBombON>");
    cl.print(relayBombON);
    cl.print("</relayBombON>");
   // cl.print("<br>");
     
    //cl.print("---Temperaturas Das Sondas---");
    //cl.print("<br>");
     
   // cl.print("Mosturacao:");
    cl.print("<tempMost>");
    cl.print(tempMost);
    cl.print("</tempMost>");
   // cl.print("<br>");

    //cl.print("Caldeira:");
    cl.print("<tempCald>");
    cl.print(tempCald);
    cl.print("</tempCald>");
    //cl.print("<br>");     
     
    //cl.print("Lavagem:");
    cl.print("<tempLav>");
    cl.print(tempLav);
    cl.print("</tempLav>");
    //cl.print("<br>");
     
   // cl.print("---Temperaturas Desejadas---");
   // cl.print("<br>");
     
   // cl.print("Mosturacao:");
    cl.print("<settedMostTemp>");
    cl.print(settedMostTemp);
    cl.print("</settedMostTemp>");
 //   cl.print("<br>");    
    
   // cl.print("Lavagem:");
    cl.print("<settedLavTemp>");
    cl.print(settedLavTemp);
    cl.print("</settedLavTemp>");
   // cl.print("<br>");
     
    //cl.print("Limiar Mosturacao:");
    cl.print("<tempThresholdMost>");
    cl.print(tempThresholdMost);
    cl.print("</tempThresholdMost>");
    //cl.print("<br>");    
     
    //cl.print("Limiar Lavagem:");
    cl.print("<tempThresholdLav>");
    cl.print(tempThresholdLav);
    cl.print("</tempThresholdLav>");
   // cl.print("<br>");  
}


// switch LED and send back HTML for LED checkbox
void ProcessCheckbox(EthernetClient cl)
{
//Serial.println(HTTP_req);
//Serial.print("Teste indexOf:");
//Serial.println(HTTP_req.indexOf("t1=1")); 
//Serial.println(settedMostTemp);
  getValue("settedMostTemp=", &settedMostTemp);
  getValue("settedLavTemp=", &settedLavTemp);
  getValue("tempThresholdMost=", &tempThresholdMost);
  getValue("tempThresholdLav=", &tempThresholdLav);
  
  getValue("relayCaldON=", &relayCaldON);  
  getValue("relayLavON=", &relayLavON);  
  getValue("relayBombON=", &relayBombON);  
}

float getTemp(OneWire ds){

    byte data[12];
    byte addr[8];
    if ( !ds.search(addr)) {
    //no more sensors on chain, reset search
    ds.reset_search();
    return -1000;
    }
    if ( OneWire::crc8( addr, 7) != addr[7]) {
    Serial.println("CRC is not valid!");
    return -1000;
    }
    if ( addr[0] != 0x10 && addr[0] != 0x28) {
    Serial.print("Device is not recognized");
    return -1000;
    }
    ds.reset();
    ds.select(addr);
    ds.write(0x44,1); 
    byte present = ds.reset();
    ds.select(addr); 
    ds.write(0xBE); 
    
    for (int i = 0; i < 9; i++) { 
    data[i] = ds.read();
    }
    
    ds.reset_search();
    
    byte MSB = data[1];
    byte LSB = data[0];
    float TRead = ((MSB << 8) | LSB); 
    float Temperature = TRead / 16;
    
    return Temperature;
}



void getValue(String tag, float * var){
    //Serial.print(tag);
  if (HTTP_req.indexOf(tag) > -1) { 
       // Serial.print("  Entrou ");
        String value = HTTP_req.substring(HTTP_req.indexOf(tag)+ tag.length(),HTTP_req.indexOf(tag)+tag.length()+5);
        char charBuf[6];
        value.toCharArray(charBuf, 6);
        float conv = atof(charBuf); 
      //  Serial.print(*var);
        *var = conv  ;
   // Serial.println("");
  }
}

void getValue(String tag, boolean * var){
  if (HTTP_req.indexOf(tag) > -1) { 
        String value = HTTP_req.substring(HTTP_req.indexOf(tag)+ tag.length(),HTTP_req.indexOf(tag)+tag.length()+1);
        char charBuf[2];
        value.toCharArray(charBuf, 2);
        float conv = atoi(charBuf); 
        *var = conv  ;
    }
}
