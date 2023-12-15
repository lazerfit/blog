CREATE TABLE `post` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `generation_time_stamp` datetime(6) DEFAULT NULL,
                        `modification_time_stamp` datetime(6) DEFAULT NULL,
                        `content` varchar(10000) COLLATE utf8mb3_bin NOT NULL,
                        `tag` varchar(500) COLLATE utf8mb3_bin NOT NULL,
                        `title` varchar(100) COLLATE utf8mb3_bin NOT NULL,
                        `views` bigint NOT NULL DEFAULT '0',
                        `category_id` bigint DEFAULT NULL,
                        `thumbnail` varchar(500) COLLATE utf8mb3_bin DEFAULT NULL,
                        `version` bigint NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;


CREATE TABLE `category` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `list_order` int DEFAULT NULL,
                            `title` varchar(30) COLLATE utf8mb3_bin NOT NULL UNIQUE ,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
