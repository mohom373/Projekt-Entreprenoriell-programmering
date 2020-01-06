# Description:

Studify is an app that helps you focus on your studies while also helping your friends get motivated for their studies as well. 

The main feature of the app is the use of the widely known Pomodoro study technique. The idea is that you (the user) set a timer between 15 
and 90 minutes to decide how long you want to study. When the timer starts a notification gets sent to specific friends that
you have chosen, to let them know that you've started a studying session. The friend can then either join and both get a shared timer 
or the notification can function as a way to tell the friend that you don't wish to be disturbed right now. 

___

# Lista med uppfyllda krav för betyg 4:
## 1. Tekniska krav (5 uppfyllda):
---
* __KRAV__: Hantering av stora/små skärmar med olika layouter (1 p)
* __Beskrivning__: I appen hanteras stora och små skrämar med hjälp av olika xml-layout-filer som innehåller egna inställningar för att anpassa innehållet.
---
* __KRAV__: Snygg användning av callbacks, dvs. anonyma listeners, men samtidigt underlättat för testning. (1 p)
* __Beskrivning__: Tydlig användning av callbacks så som setOnClicklistener för olika knappar och setNavigationItemSelectedListener för navigationsmenyn är implementarat i appen.
___
* __KRAV__: Kan hantera rotation, dvs. om enheten roteras ska skärminnehållet förbli stabilt. Samma innehåll visas som före rotationen, men nu med annan layout (1 p)
* __Beskrivning__: Rotation hanteras genom att att onSaveInstanceState och onRestoreInstanceState överlagras för att kunna spara undan olika värden som inte ska försvinna vid rotation. En extra landscape-layout xml-fil skapas också för att hantera den nya layouten.
___
* __KRAV__: Hantering av bakåtknapp, så att man aldrig hamnar konstigt vid tryck på bakåtknappen (1 p)
* __Beskrivning__: Tryck av bakåtknappen hanteras genom att onBackPressed överlagras i bland annat MainActivity och LoginActivity så att användaren ska skickas till startsidan vid tryck av backåtknapp. 
___
* __KRAV__: Åtminstone en egenskriven adapter (1 p)
* __Beskrivning__: En adapter är implenterad i koden i form av en UserScoreAdapter som ärver av ArrayAdapter. Adaptern tar data som e-post och poäng och skickar det till en listview som visas i MainActivity.

## 2. API-krav (7 uppfyllda):
___
* __KRAV__: Notifications (1 p)
* __Beskrivning__: Notifikationer implementeras med hjälp av firebase cloud messaging där det finns möjlighet att skicka notifikationer till användare, även när appen inte är igång. Man kan bestämma bland annat meddelandet samt datum och tid för när notifikationen ska skickas till användaren, genom firebase konsolen.
___
* __KRAV__: Multispråk-stöd (1 p)
* __Beskrivning__: Multispråk stöd implementeras med hjälp av att ha flera values mappar, en default (engelska) och en med det andra språket man vill stödja. Genom att koda strängarna som används i appen i string.xml filen istället för att hårdkoda dem blir det enkelt för systemet att stödja fler språk. 

___
* __KRAV__: Analytics för att logga events (1 p)
* __Beskrivning__: 5 stycken olika events loggas med hjälp av firebase analytics, där bland annat man kan se antalet gånger google sign in eller facebook sign in har använts i firebase konsolen.  

___
* __KRAV__: Enkel inloggning mot Firebase (1 p)
* __Beskrivning__: Inloggning mot Firebase implementeras med hjälp av den inbyggda funktionen för FirebaseAuth, signInWithEmailAndPassword som tar in e-post och lösenord som input. E-posten och lösenordet hämtas från två olika EditTexts.

___
* __KRAV__: Koll på inloggningsstatus för att kunna använda vissa features, t.ex. att inloggning krävs för att få se viss information i appen (1 p)
* __Beskrivning__: Appen håller koll på inloggningsstatus genom att i MainAcitivty kolla ifall någon instans av google, facebook eller Firebase login finns. Om alla tre är null är det inget konto som är inloggat och då göms ListView från användren. 

___
* __KRAV__: Tredjepartsinloggning med Facebook (1 p)
* __Beskrivning__: Facebook inloggning görs enkelt med CallbackManager. Metoden registerCallback som tillhör facebook sign-in knappen tar då in en CallbackManager och en FacebookCallback med resultatet.

___
* __KRAV__: Tredjepartsinloggning med Google (1 p)
* __Beskrivning__: Google inloggning görs enkelt med GoogleSignInClient. GoogleSignInClient tar en context och GoogleSignInOptions som bestämmer vad vi kan få från google inloggningen. 