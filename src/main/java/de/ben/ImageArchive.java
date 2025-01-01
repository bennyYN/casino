package de.ben;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

//Klasse um Bilder, welche sonst mehrfach geladen werden müssten, zu speichern und abzurufen
//→ Performanceverbesserung

public class ImageArchive {

    //Icons
    private static BufferedImage chipsImg, bigBlindImg, smallBlindImg, iconImg, cardsImg;
    private static Image scaledChipsImg, scaledBigBlindImg, scaledSmallBlindImg, scaledIconImg, scaledCardsImg;
    //Playerslots
    private static BufferedImage playerslot, emptyPlayerslot, activePlayerslot, winningPlayerslot, inactivePlayerslot, zeroPlayerslot;
    private static Image scaledPlayerslot, scaledEmptyPlayerslot, scaledActivePlayerslot, scaledWinningPlayerslot, scaledInactivePlayerslot, scaledZeroPlayerslot;
    //Lobby-Playerslots
    private static BufferedImage lobbyPlayerslot, lobbyHostPlayerslot, lobbyEmptyPlayerslot, lobbyYouPlayerslot, lobbyYouHostPlayerslot;
    private static Image scaledLobbyPlayerslot, scaledLobbyHostPlayerslot, scaledLobbyEmptyPlayerslot, scaledLobbyYouPlayerslot, scaledLobbyYouHostPlayerslot;
    //Misc
    private static BufferedImage originalBackgroundImg, darkBackgroundImg, darkblueBackgroundImg, lightBackgroundImg, scarletBackgroundImg, tableImg, potImg, darkPotImg;
    private static Image scaledBackgroundImg, scaledDarkBackgroundImg, scaledDarkblueBackgroundImg, scaledLightBackgroundImg, scaledScarletBackgroundImg, scaledTableImg, scaledPotImg, scaledDarkPotImg;
    //Cards
        //Backside
        private static BufferedImage cardBackImg, scarletCardBackImg;
        private static Image scaledCardBackImg, scaledScarletCardBackImg;
        //Clubs
        private static BufferedImage clubs2, clubs3, clubs4, clubs5, clubs6, clubs7, clubs8, clubs9, clubs10, clubsJ, clubsQ, clubsK, clubsA;
        private static Image scaledClubs2, scaledClubs3, scaledClubs4, scaledClubs5, scaledClubs6, scaledClubs7, scaledClubs8, scaledClubs9, scaledClubs10, scaledClubsJ, scaledClubsQ, scaledClubsK, scaledClubsA;
        //Diamonds
        private static BufferedImage diamonds2, diamonds3, diamonds4, diamonds5, diamonds6, diamonds7, diamonds8, diamonds9, diamonds10, diamondsJ, diamondsQ, diamondsK, diamondsA;
        private static Image scaledDiamonds2, scaledDiamonds3, scaledDiamonds4, scaledDiamonds5, scaledDiamonds6, scaledDiamonds7, scaledDiamonds8, scaledDiamonds9, scaledDiamonds10, scaledDiamondsJ, scaledDiamondsQ, scaledDiamondsK, scaledDiamondsA;
        //Hearts
        private static BufferedImage hearts2, hearts3, hearts4, hearts5, hearts6, hearts7, hearts8, hearts9, hearts10, heartsJ, heartsQ, heartsK, heartsA;
        private static Image scaledHearts2, scaledHearts3, scaledHearts4, scaledHearts5, scaledHearts6, scaledHearts7, scaledHearts8, scaledHearts9, scaledHearts10, scaledHeartsJ, scaledHeartsQ, scaledHeartsK, scaledHeartsA;
        //Spades
        private static BufferedImage spades2, spades3, spades4, spades5, spades6, spades7, spades8, spades9, spades10, spadesJ, spadesQ, spadesK, spadesA;
        private static Image scaledSpades2, scaledSpades3, scaledSpades4, scaledSpades5, scaledSpades6, scaledSpades7, scaledSpades8, scaledSpades9, scaledSpades10, scaledSpadesJ, scaledSpadesQ, scaledSpadesK, scaledSpadesA;

    public ImageArchive(){
        try{
            //Icons
            iconImg = ImageIO.read(new File("img/icon.png"));
            chipsImg = ImageIO.read(new File("img/chips.png"));
            bigBlindImg = ImageIO.read(new File("img/bigBlind.png"));
            smallBlindImg = ImageIO.read(new File("img/smallBlind.png"));
            cardsImg = ImageIO.read(new File("img/cards_icon.png"));
            //Playerslots
            playerslot = ImageIO.read(new File("img/playerslot.png"));
            emptyPlayerslot = ImageIO.read(new File("img/empty_playerslot.png"));
            activePlayerslot = ImageIO.read(new File("img/active_playerslot.png"));
            winningPlayerslot = ImageIO.read(new File("img/winning_playerslot.png"));
            inactivePlayerslot = ImageIO.read(new File("img/inactive_playerslot.png"));
            zeroPlayerslot = ImageIO.read(new File("img/zero_playerslot.png"));
            //Lobby-Playerslots
            lobbyPlayerslot = ImageIO.read(new File("img/lobby/playerslot.png"));
            lobbyHostPlayerslot = ImageIO.read(new File("img/lobby/host_playerslot.png"));
            lobbyEmptyPlayerslot = ImageIO.read(new File("img/lobby/empty_playerslot.png"));
            lobbyYouHostPlayerslot = ImageIO.read(new File("img/lobby/you_host_playerslot.png"));
            lobbyYouPlayerslot = ImageIO.read(new File("img/lobby/you_playerslot.png"));
            //Misc
            originalBackgroundImg = ImageIO.read(new File("img/background.jpg"));
            darkBackgroundImg = ImageIO.read(new File("img/background-dark.jpg"));
            darkblueBackgroundImg = ImageIO.read(new File("img/background-darkblue.jpg"));
            lightBackgroundImg = ImageIO.read(new File("img/background-light.jpg"));
            scarletBackgroundImg = ImageIO.read(new File("img/background-scarlet.jpg"));
            darkPotImg = ImageIO.read(new File("img/pot-dark.png"));
            tableImg = ImageIO.read(new File("img/table.png"));
            potImg = ImageIO.read(new File("img/pot.png"));
            //Cards
                //Backside
                cardBackImg = ImageIO.read(new File("img/cards/BACKSIDE-original.png"));
                scarletCardBackImg = ImageIO.read(new File("img/cards/BACKSIDE-scarlet.png"));
                //Clubs
                clubs2 = ImageIO.read(new File("img/cards/CLUBS 2.png"));
                clubs3 = ImageIO.read(new File("img/cards/CLUBS 3.png"));
                clubs4 = ImageIO.read(new File("img/cards/CLUBS 4.png"));
                clubs5 = ImageIO.read(new File("img/cards/CLUBS 5.png"));
                clubs6 = ImageIO.read(new File("img/cards/CLUBS 6.png"));
                clubs7 = ImageIO.read(new File("img/cards/CLUBS 7.png"));
                clubs8 = ImageIO.read(new File("img/cards/CLUBS 8.png"));
                clubs9 = ImageIO.read(new File("img/cards/CLUBS 9.png"));
                clubs10 = ImageIO.read(new File("img/cards/CLUBS 10.png"));
                clubsJ = ImageIO.read(new File("img/cards/CLUBS J.png"));
                clubsQ = ImageIO.read(new File("img/cards/CLUBS Q.png"));
                clubsK = ImageIO.read(new File("img/cards/CLUBS K.png"));
                clubsA = ImageIO.read(new File("img/cards/CLUBS A.png"));
                //Diamonds
                diamonds2 = ImageIO.read(new File("img/cards/DIAMONDS 2.png"));
                diamonds3 = ImageIO.read(new File("img/cards/DIAMONDS 3.png"));
                diamonds4 = ImageIO.read(new File("img/cards/DIAMONDS 4.png"));
                diamonds5 = ImageIO.read(new File("img/cards/DIAMONDS 5.png"));
                diamonds6 = ImageIO.read(new File("img/cards/DIAMONDS 6.png"));
                diamonds7 = ImageIO.read(new File("img/cards/DIAMONDS 7.png"));
                diamonds8 = ImageIO.read(new File("img/cards/DIAMONDS 8.png"));
                diamonds9 = ImageIO.read(new File("img/cards/DIAMONDS 9.png"));
                diamonds10 = ImageIO.read(new File("img/cards/DIAMONDS 10.png"));
                diamondsJ = ImageIO.read(new File("img/cards/DIAMONDS J.png"));
                diamondsQ = ImageIO.read(new File("img/cards/DIAMONDS Q.png"));
                diamondsK = ImageIO.read(new File("img/cards/DIAMONDS K.png"));
                diamondsA = ImageIO.read(new File("img/cards/DIAMONDS A.png"));
                //Hearts
                hearts2 = ImageIO.read(new File("img/cards/HEARTS 2.png"));
                hearts3 = ImageIO.read(new File("img/cards/HEARTS 3.png"));
                hearts4 = ImageIO.read(new File("img/cards/HEARTS 4.png"));
                hearts5 = ImageIO.read(new File("img/cards/HEARTS 5.png"));
                hearts6 = ImageIO.read(new File("img/cards/HEARTS 6.png"));
                hearts7 = ImageIO.read(new File("img/cards/HEARTS 7.png"));
                hearts8 = ImageIO.read(new File("img/cards/HEARTS 8.png"));
                hearts9 = ImageIO.read(new File("img/cards/HEARTS 9.png"));
                hearts10 = ImageIO.read(new File("img/cards/HEARTS 10.png"));
                heartsJ = ImageIO.read(new File("img/cards/HEARTS J.png"));
                heartsQ = ImageIO.read(new File("img/cards/HEARTS Q.png"));
                heartsK = ImageIO.read(new File("img/cards/HEARTS K.png"));
                heartsA = ImageIO.read(new File("img/cards/HEARTS A.png"));
                //Spades
                spades2 = ImageIO.read(new File("img/cards/SPADES 2.png"));
                spades3 = ImageIO.read(new File("img/cards/SPADES 3.png"));
                spades4 = ImageIO.read(new File("img/cards/SPADES 4.png"));
                spades5 = ImageIO.read(new File("img/cards/SPADES 5.png"));
                spades6 = ImageIO.read(new File("img/cards/SPADES 6.png"));
                spades7 = ImageIO.read(new File("img/cards/SPADES 7.png"));
                spades8 = ImageIO.read(new File("img/cards/SPADES 8.png"));
                spades9 = ImageIO.read(new File("img/cards/SPADES 9.png"));
                spades10 = ImageIO.read(new File("img/cards/SPADES 10.png"));
                spadesJ = ImageIO.read(new File("img/cards/SPADES J.png"));
                spadesQ = ImageIO.read(new File("img/cards/SPADES Q.png"));
                spadesK = ImageIO.read(new File("img/cards/SPADES K.png"));
                spadesA = ImageIO.read(new File("img/cards/SPADES A.png"));

        }catch(Exception e){
            e.printStackTrace();
        }
        rescaleImages(1, 1);
    }

    public static void rescaleImages(double xScale, double yScale){
        //Icons
        scaledIconImg = iconImg.getScaledInstance((int)(32*xScale), (int)(32*yScale), Image.SCALE_SMOOTH);
        scaledChipsImg = chipsImg.getScaledInstance((int)(16*xScale), (int)(16*xScale), Image.SCALE_SMOOTH);
        scaledBigBlindImg = bigBlindImg.getScaledInstance((int)(45*xScale), (int)(45*yScale), Image.SCALE_SMOOTH);
        scaledSmallBlindImg = smallBlindImg.getScaledInstance((int)(40*xScale), (int)(40*yScale), Image.SCALE_SMOOTH);
        scaledCardsImg = cardsImg.getScaledInstance((int)(20*xScale), (int)(20*yScale), Image.SCALE_SMOOTH);
        //Playerslots
        scaledPlayerslot = playerslot.getScaledInstance((int)(playerslot.getWidth()*xScale), (int)(playerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledEmptyPlayerslot = emptyPlayerslot.getScaledInstance((int)(emptyPlayerslot.getWidth()*xScale), (int)(emptyPlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledActivePlayerslot = activePlayerslot.getScaledInstance((int)(activePlayerslot.getWidth()*xScale), (int)(activePlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledWinningPlayerslot = winningPlayerslot.getScaledInstance((int)(winningPlayerslot.getWidth()*xScale), (int)(winningPlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledInactivePlayerslot = inactivePlayerslot.getScaledInstance((int)(inactivePlayerslot.getWidth()*xScale), (int)(inactivePlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledZeroPlayerslot = zeroPlayerslot.getScaledInstance((int)(zeroPlayerslot.getWidth()*xScale), (int)(zeroPlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        //Lobby-Playerslots
        scaledLobbyPlayerslot = lobbyPlayerslot.getScaledInstance((int)(lobbyPlayerslot.getWidth()*xScale), (int)(lobbyPlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledLobbyHostPlayerslot = lobbyHostPlayerslot.getScaledInstance((int)(lobbyHostPlayerslot.getWidth()*xScale), (int)(lobbyHostPlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledLobbyEmptyPlayerslot = lobbyEmptyPlayerslot.getScaledInstance((int)(lobbyEmptyPlayerslot.getWidth()*xScale), (int)(lobbyEmptyPlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledLobbyYouHostPlayerslot = lobbyYouHostPlayerslot.getScaledInstance((int)(lobbyYouHostPlayerslot.getWidth()*xScale), (int)(lobbyYouHostPlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledLobbyYouPlayerslot = lobbyYouPlayerslot.getScaledInstance((int)(lobbyYouPlayerslot.getWidth()*xScale), (int)(lobbyYouPlayerslot.getHeight()*yScale), Image.SCALE_SMOOTH);
        //Misc
        scaledBackgroundImg = originalBackgroundImg.getScaledInstance((int)(originalBackgroundImg.getWidth()*xScale), (int)(originalBackgroundImg.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledDarkBackgroundImg = darkBackgroundImg.getScaledInstance((int)(darkBackgroundImg.getWidth()*xScale), (int)(darkBackgroundImg.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledDarkblueBackgroundImg = darkblueBackgroundImg.getScaledInstance((int)(darkblueBackgroundImg.getWidth()*xScale), (int)(darkblueBackgroundImg.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledLightBackgroundImg = lightBackgroundImg.getScaledInstance((int)(lightBackgroundImg.getWidth()*xScale), (int)(lightBackgroundImg.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledScarletBackgroundImg = scarletBackgroundImg.getScaledInstance((int)(scarletBackgroundImg.getWidth()*xScale), (int)(scarletBackgroundImg.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledTableImg = tableImg.getScaledInstance((int)(tableImg.getWidth()*xScale), (int)(tableImg.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledPotImg = potImg.getScaledInstance((int)(potImg.getWidth()*xScale), (int)(potImg.getHeight()*yScale), Image.SCALE_SMOOTH);
        scaledDarkPotImg = darkPotImg.getScaledInstance((int)(darkPotImg.getWidth()*xScale), (int)(darkPotImg.getHeight()*yScale), Image.SCALE_SMOOTH);
        //Cards
            //Backside
            scaledCardBackImg = cardBackImg.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledScarletCardBackImg = scarletCardBackImg.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            //Clubs
            scaledClubs2 = clubs2.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubs3 = clubs3.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubs4 = clubs4.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubs5 = clubs5.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubs6 = clubs6.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubs7 = clubs7.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubs8 = clubs8.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubs9 = clubs9.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubs10 = clubs10.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubsJ = clubsJ.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubsQ = clubsQ.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubsK = clubsK.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledClubsA = clubsA.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            //Diamonds
            scaledDiamonds2 = diamonds2.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamonds3 = diamonds3.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamonds4 = diamonds4.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamonds5 = diamonds5.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamonds6 = diamonds6.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamonds7 = diamonds7.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamonds8 = diamonds8.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamonds9 = diamonds9.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamonds10 = diamonds10.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamondsJ = diamondsJ.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamondsQ = diamondsQ.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamondsK = diamondsK.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledDiamondsA = diamondsA.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            //Hearts
            scaledHearts2 = hearts2.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHearts3 = hearts3.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHearts4 = hearts4.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHearts5 = hearts5.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHearts6 = hearts6.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHearts7 = hearts7.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHearts8 = hearts8.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHearts9 = hearts9.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHearts10 = hearts10.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHeartsJ = heartsJ.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHeartsQ = heartsQ.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHeartsK = heartsK.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledHeartsA = heartsA.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            //Spades
            scaledSpades2 = spades2.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpades3 = spades3.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpades4 = spades4.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpades5 = spades5.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpades6 = spades6.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpades7 = spades7.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpades8 = spades8.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpades9 = spades9.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpades10 = spades10.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpadesJ = spadesJ.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpadesQ = spadesQ.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpadesK = spadesK.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
            scaledSpadesA = spadesA.getScaledInstance((int)(104*xScale), (int)((145+(145*Math.pow(yScale, 3.7)))/2), Image.SCALE_SMOOTH);
    }

    public static Image getImage(String imageName){
        if(imageName.contains("card:")){
            //Cards
            switch(imageName){
                //Backside
                case "card:back:Original", "card:back:Darkblue", "card:back:Dark":
                    return scaledCardBackImg;
                case "card:back:Scarlet":
                    return scaledScarletCardBackImg;
                //Clubs
                case "card:CLUBS 2":
                    return scaledClubs2;
                case "card:CLUBS 3":
                    return scaledClubs3;
                case "card:CLUBS 4":
                    return scaledClubs4;
                case "card:CLUBS 5":
                    return scaledClubs5;
                case "card:CLUBS 6":
                    return scaledClubs6;
                case "card:CLUBS 7":
                    return scaledClubs7;
                case "card:CLUBS 8":
                    return scaledClubs8;
                case "card:CLUBS 9":
                    return scaledClubs9;
                case "card:CLUBS 10":
                    return scaledClubs10;
                case "card:CLUBS J":
                    return scaledClubsJ;
                case "card:CLUBS Q":
                    return scaledClubsQ;
                case "card:CLUBS K":
                    return scaledClubsK;
                case "card:CLUBS A":
                    return scaledClubsA;
                //Diamonds
                case "card:DIAMONDS 2":
                    return scaledDiamonds2;
                case "card:DIAMONDS 3":
                    return scaledDiamonds3;
                case "card:DIAMONDS 4":
                    return scaledDiamonds4;
                case "card:DIAMONDS 5":
                    return scaledDiamonds5;
                case "card:DIAMONDS 6":
                    return scaledDiamonds6;
                case "card:DIAMONDS 7":
                    return scaledDiamonds7;
                case "card:DIAMONDS 8":
                    return scaledDiamonds8;
                case "card:DIAMONDS 9":
                    return scaledDiamonds9;
                case "card:DIAMONDS 10":
                    return scaledDiamonds10;
                case "card:DIAMONDS J":
                    return scaledDiamondsJ;
                case "card:DIAMONDS Q":
                    return scaledDiamondsQ;
                case "card:DIAMONDS K":
                    return scaledDiamondsK;
                case "card:DIAMONDS A":
                    return scaledDiamondsA;
                //Hearts
                case "card:HEARTS 2":
                    return scaledHearts2;
                case "card:HEARTS 3":
                    return scaledHearts3;
                case "card:HEARTS 4":
                    return scaledHearts4;
                case "card:HEARTS 5":
                    return scaledHearts5;
                case "card:HEARTS 6":
                    return scaledHearts6;
                case "card:HEARTS 7":
                    return scaledHearts7;
                case "card:HEARTS 8":
                    return scaledHearts8;
                case "card:HEARTS 9":
                    return scaledHearts9;
                case "card:HEARTS 10":
                    return scaledHearts10;
                case "card:HEARTS J":
                    return scaledHeartsJ;
                case "card:HEARTS Q":
                    return scaledHeartsQ;
                case "card:HEARTS K":
                    return scaledHeartsK;
                case "card:HEARTS A":
                    return scaledHeartsA;
                //Spades
                case "card:SPADES 2":
                    return scaledSpades2;
                case "card:SPADES 3":
                    return scaledSpades3;
                case "card:SPADES 4":
                    return scaledSpades4;
                case "card:SPADES 5":
                    return scaledSpades5;
                case "card:SPADES 6":
                    return scaledSpades6;
                case "card:SPADES 7":
                    return scaledSpades7;
                case "card:SPADES 8":
                    return scaledSpades8;
                case "card:SPADES 9":
                    return scaledSpades9;
                case "card:SPADES 10":
                    return scaledSpades10;
                case "card:SPADES J":
                    return scaledSpadesJ;
                case "card:SPADES Q":
                    return scaledSpadesQ;
                case "card:SPADES K":
                    return scaledSpadesK;
                case "card:SPADES A":
                    return scaledSpadesA;
                default:
                    return null;

            }
        }else{
            switch(imageName){
                //Icons
                case "chips":
                    return scaledChipsImg;
                case "bigblind":
                    return scaledBigBlindImg;
                case "smallblind":
                    return scaledSmallBlindImg;
                case "icon":
                    return scaledIconImg;
                case "cards":
                    return scaledCardsImg;
                //Playerslots
                case "playerslot":
                    return scaledPlayerslot;
                case "empty_playerslot":
                    return scaledEmptyPlayerslot;
                case "active_playerslot":
                    return scaledActivePlayerslot;
                case "winning_playerslot":
                    return scaledWinningPlayerslot;
                case "inactive_playerslot":
                    return scaledInactivePlayerslot;
                case "zero_playerslot":
                    return scaledZeroPlayerslot;
                //Lobby-Playerslots
                case "lobby:playerslot":
                    return scaledLobbyPlayerslot;
                case "lobby:empty_playerslot":
                    return scaledLobbyEmptyPlayerslot;
                case "lobby:host_playerslot":
                    return scaledLobbyHostPlayerslot;
                case "lobby:you_playerslot":
                    return scaledLobbyYouPlayerslot;
                case "lobby:you_host_playerslot":
                    return scaledLobbyYouHostPlayerslot;
                //Misc
                case "background:Original":
                    return scaledBackgroundImg;
                case "background:Dark":
                    return scaledDarkBackgroundImg;
                case "background:Darkblue":
                    return scaledDarkblueBackgroundImg;
                case "background:Light":
                    return scaledLightBackgroundImg;
                case "background:Scarlet":
                    return scaledScarletBackgroundImg;
                case "table":
                    return scaledTableImg;
                case "pot", "pot:", "pot:Original", "pot:Scarlet", "pot:Dark", "pot:Darkblue":
                    return scaledPotImg;
                case "pot:Light":
                    return scaledDarkPotImg;
                default:
                    return null;
            }
        }

    }

    public static BufferedImage getUnprocessedImage(String imageName){
        if(imageName.contains("card:")) {
            //Cards
            switch (imageName) {
                //Backside
                case "card:back":
                    return cardBackImg;
                //Clubs
                case "card:CLUBS 2":
                    return clubs2;
                case "card:CLUBS 3":
                    return clubs3;
                case "card:CLUBS 4":
                    return clubs4;
                case "card:CLUBS 5":
                    return clubs5;
                case "card:CLUBS 6":
                    return clubs6;
                case "card:CLUBS 7":
                    return clubs7;
                case "card:CLUBS 8":
                    return clubs8;
                case "card:CLUBS 9":
                    return clubs9;
                case "card:CLUBS 10":
                    return clubs10;
                case "card:CLUBS J":
                    return clubsJ;
                case "card:CLUBS Q":
                    return clubsQ;
                case "card:CLUBS K":
                    return clubsK;
                case "card:CLUBS A":
                    return clubsA;
                //Diamonds
                case "card:DIAMONDS 2":
                    return diamonds2;
                case "card:DIAMONDS 3":
                    return diamonds3;
                case "card:DIAMONDS 4":
                    return diamonds4;
                case "card:DIAMONDS 5":
                    return diamonds5;
                case "card:DIAMONDS 6":
                    return diamonds6;
                case "card:DIAMONDS 7":
                    return diamonds7;
                case "card:DIAMONDS 8":
                    return diamonds8;
                case "card:DIAMONDS 9":
                    return diamonds9;
                case "card:DIAMONDS 10":
                    return diamonds10;
                case "card:DIAMONDS J":
                    return diamondsJ;
                case "card:DIAMONDS Q":
                    return diamondsQ;
                case "card:DIAMONDS K":
                    return diamondsK;
                case "card:DIAMONDS A":
                    return diamondsA;
                //Hearts
                case "card:HEARTS 2":
                    return hearts2;
                case "card:HEARTS 3":
                    return hearts3;
                case "card:HEARTS 4":
                    return hearts4;
                case "card:HEARTS 5":
                    return hearts5;
                case "card:HEARTS 6":
                    return hearts6;
                case "card:HEARTS 7":
                    return hearts7;
                case "card:HEARTS 8":
                    return hearts8;
                case "card:HEARTS 9":
                    return hearts9;
                case "card:HEARTS 10":
                    return hearts10;
                case "card:HEARTS J":
                    return heartsJ;
                case "card:HEARTS Q":
                    return heartsQ;
                case "card:HEARTS K":
                    return heartsK;
                case "card:HEARTS A":
                    return heartsA;
                //Spades
                case "card:SPADES 2":
                    return spades2;
                case "card:SPADES 3":
                    return spades3;
                case "card:SPADES 4":
                    return spades4;
                case "card:SPADES 5":
                    return spades5;
                case "card:SPADES 6":
                    return spades6;
                case "card:SPADES 7":
                    return spades7;
                case "card:SPADES 8":
                    return spades8;
                case "card:SPADES 9":
                    return spades9;
                case "card:SPADES 10":
                    return spades10;
                case "card:SPADES J":
                    return spadesJ;
                case "card:SPADES Q":
                    return spadesQ;
                case "card:SPADES K":
                    return spadesK;
                case "card:SPADES A":
                    return spadesA;
                default:
                    return null;
            }
        }else{
                switch (imageName) {
                    //Icons
                    case "chips":
                        return chipsImg;
                    case "bigblind":
                        return bigBlindImg;
                    case "smallblind":
                        return smallBlindImg;
                    case "icon":
                        return iconImg;
                    case "cards":
                        return cardsImg;
                    //Playerslots
                    case "playerslot":
                        return playerslot;
                    case "empty_playerslot":
                        return emptyPlayerslot;
                    case "active_playerslot":
                        return activePlayerslot;
                    case "winning_playerslot":
                        return winningPlayerslot;
                    case "inactive_playerslot":
                        return inactivePlayerslot;
                    case "zero_playerslot":
                        return zeroPlayerslot;
                    //Misc
                    case "background:Original":
                        return originalBackgroundImg;
                    case "background:Dark":
                        return darkBackgroundImg;
                    case "background:Darkblue":
                        return darkblueBackgroundImg;
                    case "background:Light":
                        return lightBackgroundImg;
                    case "background:Scarlet":
                        return scarletBackgroundImg;
                    case "table":
                        return tableImg;
                    case "pot", "pot:", "pot:Original", "pot:Scarlet", "pot:Dark", "pot:Darkblue":
                        return potImg;
                    case "pot:Light":
                        return darkPotImg;
                    //Cards
                    case "card:back":
                        return cardBackImg;
                    default:
                        return null;
                }
        }
    }
}
