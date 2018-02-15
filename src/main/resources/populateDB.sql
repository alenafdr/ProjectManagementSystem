
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `DBProjectManagementSystem`
--

--
-- Дамп данных таблицы `companies`
--

INSERT INTO `companies` (`id`, `name`) VALUES
(1, 'company1'),
(2, 'company2'),
(3, 'company3'),
(4, 'company4'),
(5, 'company5');

--
-- Дамп данных таблицы `customers`
--

INSERT INTO `customers` (`id`, `name`) VALUES
(1, 'customer1'),
(2, 'customer2'),
(3, 'customer3'),
(4, 'customer4'),
(5, 'customer5');


--
-- Дамп данных таблицы `developers`
--

INSERT INTO `developers` (`id`, `name`) VALUES
(1, 'developer2'),
(2, 'developer3'),
(3, 'developer5');

--
-- Дамп данных таблицы `projects`
--

INSERT INTO `projects` (`id`, `name`) VALUES
(1, 'project1'),
(2, 'project2'),
(3, 'project3'),
(4, 'project4'),
(5, 'project5'),
(6, 'project6');


--
-- Дамп данных таблицы `skills`
--

INSERT INTO `skills` (`id`, `name`) VALUES
(1, 'java'),
(2, 'c++'),
(3, 'sql'),
(4, 'junit');

--
-- Дамп данных таблицы `companies_projects`
--

INSERT INTO `companies_projects` (`id`, `company_id`, `project_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 1),
(4, 2, 3),
(5, 3, 4),
(6, 4, 5),
(7, 4, 6),
(8, 5, 5);

--
-- Дамп данных таблицы `projects_developers`
--

INSERT INTO `projects_developers` (`id`, `project_id`, `developer_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 1),
(4, 2, 3),
(5, 3, 2),
(6, 3, 3),
(7, 4, 1),
(8, 4, 3),
(9, 5, 2),
(10, 5, 3);

--
-- Дамп данных таблицы `developers_skills`
--

INSERT INTO `developers_skills` (`id`, `developer_id`, `skill_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 3),
(4, 2, 1),
(5, 3, 4),
(6, 3, 2);

--
-- Дамп данных таблицы `customers_projects`
--

INSERT INTO `customers_projects` (`id`, `customer_id`, `project_id`) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 3, 4),
(5, 4, 5),
(6, 5, 6);



/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
