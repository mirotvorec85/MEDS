SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;


DROP TABLE IF EXISTS `character`;
CREATE TABLE `character` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(16) NOT NULL,
  `password_hash` VARCHAR(32) NOT NULL,
  `last_login_ip` VARCHAR(16) NOT NULL DEFAULT '-',
  `last_login_date` INT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_achievement`;
CREATE TABLE `character_achievement` (
  `character_id` INT UNSIGNED NOT NULL,
  `achievement_id` INT UNSIGNED NOT NULL,
  `progress` INT UNSIGNED NOT NULL DEFAULT '0',
  `complete_date` INT UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`character_id`,`achievement_id`),
  FOREIGN KEY (`achievement_id`) REFERENCES `achievement` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_currency`;
CREATE TABLE `character_currency` (
  `character_id` INT UNSIGNED NOT NULL,
  `currency_id` INT UNSIGNED NOT NULL,
  `amount` INT UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`character_id`,`currency_id`),
  FOREIGN KEY (`currency_id`) REFERENCES `currency` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_guild`;
CREATE TABLE `character_guild` (
  `character_id` INT UNSIGNED NOT NULL,
  `guild_id` INT UNSIGNED NOT NULL,
  `level` TINYINT UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`character_id`,`guild_id`),
  FOREIGN KEY (`guild_id`) REFERENCES `guild` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_info`;
CREATE TABLE `character_info` (
  `character_id` INT UNSIGNED NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `avatar_id` INT UNSIGNED NOT NULL DEFAULT '0',
  `race` TINYINT UNSIGNED NOT NULL,
  `clan_id` INT UNSIGNED DEFAULT NULL,
  `clan_status` TINYINT UNSIGNED NOT NULL DEFAULT '0',
  `base_con` INT UNSIGNED NOT NULL DEFAULT '0',
  `base_str` INT UNSIGNED NOT NULL DEFAULT '0',
  `base_dex` INT UNSIGNED NOT NULL DEFAULT '0',
  `base_int` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_con` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_str` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_dex` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_int` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_dam` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_abs` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_chth` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_ac` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_chtc` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_mdam` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_hp` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_mp` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_hpregen` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_mpregen` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_fireresist` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_frostresist` INT UNSIGNED NOT NULL DEFAULT '0',
  `guild_shockresist` INT UNSIGNED NOT NULL DEFAULT '0',
  `exp` INT UNSIGNED NOT NULL DEFAULT '0',
  `relig_exp` INT UNSIGNED NOT NULL DEFAULT '0',
  `level` INT UNSIGNED NOT NULL DEFAULT '0',
  `relig_level` INT UNSIGNED NOT NULL DEFAULT '0',
  `location_id` INT UNSIGNED DEFAULT NULL,
  `settings` INT UNSIGNED NOT NULL DEFAULT '0',
  `statuses` INT UNSIGNED NOT NULL DEFAULT '0',
  `practise_value` DOUBLE NOT NULL DEFAULT '0',
  `notepad` TEXT NOT NULL,
  PRIMARY KEY (`character_id`),
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`location_id`) REFERENCES `location` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_inn_item`;
CREATE TABLE `character_inn_item` (
  `character_id` INT UNSIGNED NOT NULL,
  `item_template_id` INT UNSIGNED NOT NULL,
  `modification` INT UNSIGNED NOT NULL DEFAULT '0',
  `durability` INT UNSIGNED NOT NULL DEFAULT '0',
  `count` INT UNSIGNED NOT NULL DEFAULT '1',
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`item_template_id`) REFERENCES `item_template` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_inventory_item`;
CREATE TABLE `character_inventory_item` (
  `character_id` INT UNSIGNED NOT NULL,
  `slot` TINYINT UNSIGNED NOT NULL,
  `item_template_id` INT UNSIGNED NOT NULL,
  `modification` INT UNSIGNED NOT NULL DEFAULT '0',
  `durability` INT UNSIGNED NOT NULL DEFAULT '0',
  `count` INT UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (`character_id`,`slot`),
  FOREIGN KEY (`item_template_id`) REFERENCES `item_template` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_profession`;
CREATE TABLE `character_profession` (
  `character_id` INT UNSIGNED NOT NULL,
  `profession_id` INT UNSIGNED NOT NULL,
  `level` TINYINT UNSIGNED NOT NULL DEFAULT '0',
  `experience` DOUBLE NOT NULL DEFAULT '0',
  PRIMARY KEY (`character_id`,`profession_id`),
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_quest`;
CREATE TABLE `character_quest` (
  `character_id` INT UNSIGNED NOT NULL,
  `skill_id` INT UNSIGNED NOT NULL,
  `level` TINYINT UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (`character_id`,`skill_id`),
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_skill`;
CREATE TABLE `character_skill` (
  `character_id` INT UNSIGNED NOT NULL,
  `skill_id` INT UNSIGNED NOT NULL,
  `level` TINYINT UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (`character_id`,`skill_id`),
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_spell`;
CREATE TABLE `character_spell` (
  `character_id` INT UNSIGNED NOT NULL,
  `spell_id` INT UNSIGNED NOT NULL,
  `level` TINYINT UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (`character_id`,`spell_id`),
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`spell_id`) REFERENCES `spell` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `character_quest`;
CREATE TABLE `character_quest` (
  `character_id` INT UNSIGNED NOT NULL,
  `quest_template_id` INT UNSIGNED NOT NULL,
  `status` INT UNSIGNED NOT NULL DEFAULT 0,
  `progress` INT NOT NULL DEFAULT 0,
  `timer` INT UNSIGNED NOT NULL DEFAULT 0,
  `is_tracked` TINYINT UNSIGNED NOT NULL DEFAULT 0,
  `accept_date` INT UNSIGNED NOT NULL DEFAULT 0,
  `complete_date` INT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`character_id`,`quest_template_id`),
  FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`quest_template_id`) REFERENCES `quest_template` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
