##instructions:
sync docker-compose.yml file using intellij
go to intellij services and press start all
![Start-all_image](img/start-all_image.png)
go to terminal and run : docker-compose run --rm --service-ports app.
The Java application displays a menu to let a user choose a report.
this menu will show.
![Menu_image](img/menu.png)


![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/ResubmitSem/Sem/main.yml?branch=main)

[![LICENSE](https://img.shields.io/github/license/ResubmitSem/sem.svg?style=flat-square)](https://github.com/ResubmitSem/sem/blob/master/LICENSE)

[![Releases](https://img.shields.io/github/release/ResubmitSem/sem/all.svg?style=flat-square)](https://github.com/ResubmitSem/sem/releases)

| **ID** | **Report**                                                                 | **Met** | **Result** |
|--------|-----------------------------------------------------------------------------|-------------|------------|
| 1      | All the countries in the world organised by largest population to smallest |         yes     | ![report1](img/report1.PNG) |
| 2      | All the cities in the world organised by largest population to smallest    |      yes       | ![report2](img/report2.PNG) |
| 3      | All the capital cities in the world organised by largest population to smallest |     yes    | ![report3](img/report3.PNG) |
| 4      | The top N populated cities in the world where N is provided by the user    |        yes     | ![report4](img/report4.PNG) |
| 5      | The population of people, people living in cities, and people not living in cities in each country | yes | ![report5](img/report5.PNG) |
| 6      | The number of Chinese speakers with percentage of world population         |      yes       | ![report6](img/report6.PNG) |
| 7      | The number of English speakers with percentage of world population         |        yes     | ![report7](img/report7.PNG) |
| 8      | The number of Spanish speakers with percentage of world population         |       yes      | ![report8](img/report8.PNG) |

![declaration](img/declaration.png)

Asked to explain github actions because the job kept being queued.
Asked how to run docker-compose and allow the user to interact with the menu of the report.