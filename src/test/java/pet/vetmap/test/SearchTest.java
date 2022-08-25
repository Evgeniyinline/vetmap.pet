package pet.vetmap.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.commons.codec.language.bm.Lang;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class SearchTest {


    @ValueSource(strings = {"Санкт-Петербург","Москва","Новосибирск"})
    @ParameterizedTest(name = "Поиск статьи про {0} в Википедии" )
    void searchCityTest(String testCity) {
        open("https://ru.wikipedia.org");
        $(".vector-search-box-input").setValue(testCity);
        $("#searchButton").click();
        $$("#bodyContent").shouldBe(CollectionCondition.itemWithText(testCity));
    }


    @CsvSource(value = {
            "Санкт-Петербург | Расположен на северо-западе страны на побережье",
            "Москва | Расположена на западе России, на реке ",
            "Новосибирск | Город является центром",

    },delimiter = '|')
    @ParameterizedTest(name = "Поиск статьи про {0} с текстом {1} в Википедии" )
    void complexSearchCityTest(String testCity,String expectedResult) {
        open("https://ru.wikipedia.org");
        $(".vector-search-box-input").setValue(testCity);
        $("#searchButton").click();
        $$("#bodyContent")
                .first()
                .shouldHave(text(expectedResult));

    }

    static Stream<Arguments> appTorrentTest() {
        return Stream.of(
                Arguments.of("Русский",List.of("Программы", "Игры", "Расширения", "Статьи", "Версии ОС", "Товары")),
                Arguments.of("English", List.of("Programs", "Games", "Extensions", "Articles", "OS versions", "Products")),
                Arguments.of("German", List.of("Programme", "Spiele", "Erweiterungen", "Artikel", "OS-Versionen", "Produkte"))

        );
    }
//пристрелить кнопку
    @MethodSource()
    @ParameterizedTest(name = "Для локали {0} отображаются кнопки меню {1}")
    void appTorrentTest(String lang,List<String> expectedButtons) {
        open("https://appstorrent.ru");
        $(".lang").click();
        $$("#langDropdown a").find(text(lang)).click();
        $$(".container a").shouldHave((CollectionCondition) expectedButtons);

    }

}



