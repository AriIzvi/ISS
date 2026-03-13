
|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-1: Autentificare utilizator**|   |   |
|Primary actor|**Client**|Secondary actors|**Administrator**|
|Description|Permite accesul utilizatorului în sistem prin verificarea identității.|   |   |
|Trigger|Utilizatorul accesează o funcție care necesită cont sau apasă „Login”.|   |   |
|Preconditions|PRE-1. Utilizatorul are un cont creat în sistem.|   |   |
|Postconditions|POST-1. Utilizatorul este logat și are acces la funcțiile securizate.|   |   |
|Normal flow|1. Sistemul afișează formularul de login (email și parolă).<br><br>2. Clientul introduce email-ul și parola.<br><br>3. Sistemul validează datele în baza de date.<br><br>4. Sistemul afișează pagina principală personalizată.|   |   |
|Alternative flows|1.1 Utilizatorul nu are cont: Clientul alege „Register”. Sistemul redirecționează către UC de Înregistrare.|   |   |
|Exceptions|1.0.E1 Date incorecte: Sistemul afișează mesaj de eroare și solicită reintroducerea datelor.|   |   |

|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-2: Căutare carte**|   |   |
|Primary actor|**Client**|Secondary actors|-|
|Description|Utilizatorul caută cărți în catalog după diverse criterii.|   |   |
|Trigger|Clientul introduce un text în bara de căutare.|   |   |
|Preconditions|PRE-1. Catalogul de cărți nu este gol.|   |   |
|Postconditions|POST-1. Sistemul afișează lista rezultatelor care corespund criteriilor.|   |   |
|Normal flow|1. Sistemul afișează bara de căutare pe pagina principală.<br><br>2. Clientul introduce titlul sau autorul dorit.<br><br>3. Sistemul interoghează baza de date.<br><br>4. Sistemul afișează o listă de cărți ce conțin textul introdus.|   |   |
|Alternative flows|2.1 Căutare după gen: Clientul selectează un gen din meniu. Sistemul filtrează catalogul după genul respectiv.|   |   |
|Exceptions|2.0.E1 Niciun rezultat găsit: Sistemul informează utilizatorul că nu există cărți pentru textul introdus.|   |   |

|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-3: Vizualizare detalii carte**|   |   |
|Primary actor|Client|Secondary actors|-|
|Description|Afișarea informațiilor complete despre o carte selectată (preț, autor, descriere).|   |   |
|Trigger|Clientul dă click pe o carte din listă sau rezultate.|   |   |
|Preconditions|PRE-1. O listă de cărți este deja afișată.|   |   |
|Postconditions|POST-1. Detaliile complete sunt afișate pe ecran.|   |   |
|Normal flow|1. Clientul selectează o carte specifică.<br><br>2. Sistemul preia toate atributele cărții din baza de date.<br><br>3. Sistemul afișează pagina cărții cu titlu, autor, descriere, preț și recenzii.|   |   |
|Alternative flows|-|   |   |
|Exceptions|3.0.E1 Cartea nu mai există: Sistemul anunță că produsul a fost retras din catalog.|   |   |

|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-4: Gestionare coș de cumpărături**|   |   |
|Primary actor|Client|Secondary actors|-|
|Description|Permite adăugarea sau eliminarea produselor digitale dintr-o listă temporară.|   |   |
|Trigger|Clientul apasă butonul „Adaugă în coș” sau „Șterge”.|   |   |
|Preconditions|PRE-1. Clientul a vizualizat detaliile unei cărți.|   |   |
|Postconditions|POST-1. Conținutul coșului este actualizat.|   |   |
|Normal flow|1. Clientul solicită adăugarea unei cărți în coș.<br><br>2. Sistemul verifică dacă produsul este deja în coș.<br><br>3. Sistemul salvează produsul în sesiunea/coșul utilizatorului.<br><br>4. Sistemul afișează coșul actualizat.|   |   |
|Alternative flows|4.1 Ștergere: Clientul apasă „Șterge” în pagina coșului. Sistemul elimină produsul și reîmprospătează lista.|   |   |
|Exceptions|4.0.E1 Produs deja existent: Sistemul informează că un produs digital poate fi cumpărat o singură dată.|   |   |

|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-5: Finalizare comandă**|   |   |
|Primary actor|**Client**|Secondary actors|Sistem Bancar|
|Description|Procesul de plată și transfer al drepturilor de acces către client.|   |   |
|Trigger|Clientul apasă „Checkout” din pagina coșului.|   |   |
|Preconditions|PRE-1. Clientul este autentificat.<br><br>PRE-2. Coșul nu este gol.|   |   |
|Postconditions|POST-1. Comanda este marcată ca „Plătită”.<br><br>POST-2. Cărțile sunt transferate în Librăria Personală.|   |   |
|Normal flow|1. Sistemul afișează sumarul comenzii și totalul de plată.<br><br>2. Clientul confirmă dorința de a plăti.<br><br>3. Sistemul deleagă plata către UC-6 (Validare plată).<br><br>4. Sistemul primește confirmarea succesului.<br><br>5. Sistemul adaugă cărțile în Biblioteca Personală a Clientului.|   |   |
|Alternative flows|-|   |   |
|Exceptions|5.0.E1 Eroare la transfer digital: Plata a fost făcută, dar scrierea în bibliotecă a eșuat. Sistemul notifică administratorul.|   |   |

|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-6: Validare plată**|   |   |
|Primary actor|**Sistem (invocat de UC-5)**|Secondary actors|**Sistem Bancar**|
|Description|Verificarea validității cardului și a fondurilor prin partener extern.|   |   |
|Trigger|UC-5 solicită autorizarea plății.|   |   |
|Preconditions|PRE-1. Datele cardului au fost introduse.|   |   |
|Postconditions|POST-1. Tranzacția este aprobată sau respinsă.|   |   |
|Normal flow|1. Sistemul trimite detaliile tranzacției către Sistemul Bancar.<br><br>2. Sistemul Bancar procesează cererea.<br><br>3. Sistemul Bancar confirmă că tranzacția este autorizată.<br><br>4. Sistemul returnează succes către UC-5.|   |   |
|Alternative flows|-|   |   |
|Exceptions|6.0.E1 Fonduri insuficiente: Sistemul Bancar respinge plata. Sistemul informează Clientul.|   |   |

|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-7: Gestionare bibliotecă personală**|   |   |
|Primary actor|**Client**|Secondary actors|-|
|Description|Vizualizarea și accesarea cărților digitale achiziționate anterior.|   |   |
|Trigger|Clientul accesează secțiunea „My Books” sau „Librăria mea”.|   |   |
|Preconditions|PRE-1. Clientul este autentificat.|   |   |
|Postconditions|POST-1. Lista cărților deținute este afișată.|   |   |
|Normal flow|1. Sistemul interoghează tabelul de librărie pentru ID-ul utilizatorului logat.<br><br>2. Sistemul preia lista de cărți cumpărate.<br><br>3. Sistemul afișează coperțile și butoanele de „Citește acum” pentru fiecare carte.|   |   |
|Alternative flows|-|   |   |
|Exceptions|7.0.E1 Biblioteca goală: Sistemul sugerează utilizatorului să viziteze magazinul pentru prima achiziție.|   |   |

|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-8: Adăugare recenzie**|   |   |
|Primary actor|**Client**|Secondary actors|-|
|Description|Utilizatorul oferă un rating și un comentariu unei cărți cumpărate.|   |   |
|Trigger|Clientul apasă „Scrie o recenzie” în pagina detaliilor cărții.|   |   |
|Preconditions|PRE-1. Utilizatorul a cumpărat cartea respectivă.|   |   |
|Postconditions|POST-1. Recenzia este vizibilă pentru toți utilizatorii.|   |   |
|Normal flow|1. Sistemul afișează un formular cu sistem de rating (stele) și casetă text.<br><br>2. Clientul completează datele și apasă „Trimite”.<br><br>3. Sistemul salvează recenzia și o asociază cărții și utilizatorului.<br><br>4. Sistemul reîmprospătează pagina cu noua recenzie afișată.|   |   |
|Alternative flows|-|   |   |
|Exceptions|8.0.E1 Utilizatorul nu a cumpărat cartea: Sistemul blochează trimiterea și informează că doar cumpărătorii pot lăsa recenzii.|   |   |

|   |   |   |   |
|---|---|---|---|
|ID and name|**UC-9: Import date cărți**|   |   |
|Primary actor|Administrator|Secondary actors|-|
|Description|Încărcarea în masă a noilor titluri în catalog de către personalul tehnic.|   |   |
|Trigger|Administratorul accesează panoul de admin și alege „Import Catalog”.|   |   |
|Preconditions|PRE-1. Administratorul este logat cu drepturi speciale.|   |   |
|Postconditions|POST-1. Baza de date a catalogului este populată cu noi înregistrări.|   |   |
|Normal flow|1. Sistemul solicită încărcarea unui fișier (ex: CSV sau JSON).<br><br>2. Administratorul selectează fișierul de pe disc.<br><br>3. Sistemul parsează datele și verifică integritatea lor.<br><br>4. Sistemul salvează noile cărți în baza de date și afișează un raport de succes.|   |   |
|Alternative flows|-|   |   |
|Exceptions|9.0.E1 Format fișier invalid: Sistemul respinge importul și indică erorile de formatare din fișier.|   |   |