# myCarRent
<p> Aplikacja będzie napisana w Springu z wykorzystaniem następujących technologii:</p>
<ul>
    <li> Spring MVC  </li>
    <li> Spring Data JPA  </li>
    <li> Spring Security  </li>
    <li> EDIT (28.11.2019) - frontend będzie zrobiony za pomocą bootstrapa </li>
</ul>
<br>
<p> 29.08.2019 </p>
<ul>
    <li> utworzenie projektu, stworzenie pakietów (configuration, controller, model, repository, service) </li>
    <li> stworzenie klas koniecznych do rejestracji użytkownika </li>
</ul>
<p> 31.08.2019 </p>
<ul>
    <li> dodanie walidacji pól przy rejestracji użytkownika (np. hasło musi posiadać przynajmniej jeden znak specjalny itp.) </li>
    <li> w formularzu dodano sprawdzenie czy podany email juz istnieje </li>
</ul>
<p> 07.09.2019 </p>
<ul>
    <li> wyświetlanie wszystkich użytkowników z możliwością usuwania </li>
</ul>
<p> 12.09.2019 </p>
<ul>
    <li> stworzenie klas Car i CarPicture, enums</li>
</ul>
<p> 15.09.2019 </p>
<ul>
    <li> możliwość dodania pojazdu</li>
    <li> wyświetlenie wszystkich pojazdów</li>
</ul>
<p> 05.10.2019 </p>
    <ul>
        <li> możliwośc dodania wyposażenia dla pojazdu </li>
        <li> filtrowanie pojazdów po wybranych kategoriach </li>
        <li> lista użytkowników z możliwościa ich usuwania oraz edytowania </li>
        <li> dodanie paginacji do listy użytkowników </li>
</ul>
<p> 17.10.2019 </p>
    <ul>
        <li> możliwośc zamawiania pojazdu </li>
        <li> dodawanie komentarzy dla pojazdów </li>
        <li> specjalne/promocyjne oferty pojazdów  </li>
</ul>
<p> 20.10.2019 </p>
    <ul>
        <li> dodane stylów css dla login/register html </li>
        <li> dodawanie/usunięcie wyposażenia które potem jest wybierane z listy przy dodawaniu pojazdu </li>
</ul>
<p> 27.10.2019 </p>
    <ul>
        <li> zmieniłem logikę dodania pojazdu </li>
        <li> teraz zdjęcia można dodawać tylko z pliku, każdy pojazd posiada własny folder w którym są trzymane zdjęcia, wyposażenie jest wybierane w listy </li>
        <li> można teraz usuwać pojazdy </li>
        <li> edycja pojazdu w toku </li>
</ul>
<p> 09.11.2019 </p>
    <ul>
        <li> zrobiona edycja pojazdu, można edytować dane z formularza, dodać/usuwać zdjęcia, validacja błedów, limit zdjęć </li>
        <li> refactoring dla klas CarController i CarService</li>
        <li> dadałem paginacje dla listy pojazdów oraz zamówień </li>
        <li> dodałem scheduler sprawdzający status zamówień, który jest uruchamiany przy starcie aplikacji oraz codziennie o północy </li>
        <li> dodałem bootstrap dla kilku formularzy (rejestracja, logowanie, dodanie pojazdu, lista wszystkich pojazdów) </li>
        <li> implementacja spring security w toku </li>
</ul>
<p> 28.11.2019 </p>
    <ul>
        <li> zrobiony security (logowanie, wylogowanie, sprawdzanie czy uzytkownik jest adminem, nie obsługuje jeszcze wszystkich url) z rolami (ROLE_USER, ROLE_ADMIN) </li>
        <li> zrobione szyfrowanie hasła w bazie </li> 
        <li> dodana możliwość przywracania hasła </li>
    </ul>
<p> 08.12.2019 </p>
    <ul>
        <li> dodanie możliwości dodawania/zabierania praw admina oraz deaktywacja/aktywacja uzytkownika</li>
        <li> tworzenie frontendu dla widoków w toku </li> 
    </ul>    
<p> 06.01.2020</p>
    <ul>
        <li> zrobione wszystkie widoki </li>
        <li> dodana validacja na frontendzie </li>
        <li> dodano moduł statystyk które pokazuje podsumowanie miesiąca (zysk, ile złożono zamówień, ile rezerwacji anulowano, statusy pojazdów itp. ) </li>
        <li> dodano kare za anulowanie rezerwacji pojazdu; dzień, dwa lub trzy przed startem rezerwacji </li>
        <li> refaktoring kodu </li>
    </ul>        
<img src="https://github.com/jakub87/myCarRent/blob/master/src/main/resources/static/images/2020-01-06_19h28_10.png" alt="" width="300" height="300">  
    
    
    