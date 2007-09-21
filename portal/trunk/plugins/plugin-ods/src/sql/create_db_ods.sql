drop table if exists ods_arrondissement_du_pdd;

drop table if exists ods_categorie_deliberation;

drop table if exists ods_co_emettrice;

drop table if exists ods_commission;

drop table if exists ods_critere;

drop table if exists ods_direction;

drop table if exists ods_elmt_releve_travaux;

drop table if exists ods_elu;

drop table if exists ods_entree_commission;

drop table if exists ods_entree_elus;

drop table if exists ods_entree_ordre_jour;

drop table if exists ods_expression_usuelle;

drop table if exists ods_fascicule;

drop table if exists ods_fichier;

drop table if exists ods_fichier_physique;

drop table if exists ods_formation_conseil;

drop table if exists ods_groupe;

drop table if exists ods_historique;

drop table if exists ods_modele_ordre_jour;

drop table if exists ods_nature_dossier;

drop table if exists ods_odj;

drop table if exists ods_panier;

drop table if exists ods_pdd;

drop table if exists ods_releve_travaux;

drop table if exists ods_requete;

drop table if exists ods_seance;

drop table if exists ods_statut;

drop table if exists ods_tourniquet;

drop table if exists ods_type_de_critere;

drop table if exists ods_type_document;

drop table if exists ods_type_ordre_jour;

drop table if exists ods_utilisateur;

drop table if exists ods_va_depose_par;

drop table if exists ods_va_rattache_pdd;

drop table if exists ods_voeu_amendement;

drop table if exists ods_front_indexer;

drop table if exists ods_index_task;

drop table if exists ods_horodatage_publication;

drop table if exists ods_horodatage_notifications;

drop table if exists ods_horodatage_notifications_utilisateurs;

drop table if exists ods_publication_parisfr;

/*==============================================================*/
/* Table : ods_horodatage_publication                            */
/*==============================================================*/
create table ods_horodatage_publication
(
   id_trace                       int(8)                   not null,
   date_trace                     timestamp,
   date_seance                    timestamp,
   famille_doc                    varchar(100),
   id_db_ods                      int(8),
   reference                      varchar(20),
   date_publication               timestamp,
   intitule                       varchar(100),
   version                        int(8),
   action                         tinyint(1),
   signatures                     text,
   primary key (id_trace)
)
comment = "Contient les traces de publication/dépublication"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_horodatage_notifications                            */
/*==============================================================*/
create table ods_horodatage_notifications
(
   id_trace                       int(8)                   not null,
   date_trace                     timestamp,
   objet                          varchar(255),
   contenu                        text,
   signature                      text,
   primary key (id_trace)
)
comment = "Contient les traces de notification de mail"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_horodatage_notifications_utilisateurs                            */
/*==============================================================*/
create table ods_horodatage_notifications_utilisateurs
(
   id_trace                       int(8)                   not null,
   id_utilisateur                 varchar(50)                    not null,
   date_envoi                     timestamp,
   destinataires                  varchar(255),
   primary key (id_trace, id_utilisateur)
)
comment = "Contient les traces de notification de mail pour un utilisateur"
type = InnoDB default charset=utf8;


/*==============================================================*/
/* Table : ods_arrondissement_du_pdd                            */
/*==============================================================*/
create table ods_arrondissement_du_pdd
(
   id_arrondissement              int(8)                   not null,
   id_pdd                         int(8)                   not null,
   arrondissement                 int(2),
   primary key (id_arrondissement)
)
comment = "Contient les arrondissements du PDD"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : localise_dans_fk                                     */
/*==============================================================*/
create index localise_dans_fk on ods_arrondissement_du_pdd
(
   id_pdd
);

/*==============================================================*/
/* Table : ods_categorie_deliberation                           */
/*==============================================================*/
create table ods_categorie_deliberation
(
   id_categorie                   int(8)                   not null,
   code_categorie                 int(8)                   not null,
   libelle_categorie              text                           not null,
   actif                          bool                           not null,
   primary key (id_categorie)
)
comment = "Contient les catégories de délibération"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_co_emettrice                                     */
/*==============================================================*/
create table ods_co_emettrice
(
   id_pdd                         int(8)                   not null,
   id_direction                   int(8)                   not null,
   code_projet                    varchar(20),
   primary key (id_pdd, id_direction)
)
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : ods_co_emettrice_fk                                  */
/*==============================================================*/
create index ods_co_emettrice_fk on ods_co_emettrice
(
   id_pdd
);

/*==============================================================*/
/* Index : ods_co_emettrice2_fk                                 */
/*==============================================================*/
create index ods_co_emettrice2_fk on ods_co_emettrice
(
   id_direction
);

/*==============================================================*/
/* Table : ods_commission                                       */
/*==============================================================*/
create table ods_commission
(
   id_commission                  int(8)                   not null,
   numero_commission              int(8)                   not null,
   libelle_commission             text                           not null,
   actif                          bool,
   primary key (id_commission)
)
comment = "Contient les commissions"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_critere                                          */
/*==============================================================*/
create table ods_critere
(
   id_critere                     int(8)                   not null,
   id_requete                     int(8)                   not null,
   id_type_critere                int(8)                   not null,
   valeur_critere                 varchar(100)                   not null,
   primary key (id_critere)
)
comment = "Contient la valeur d'un critère de la requête personnelle de l'utilisateur"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : est_composee_de_fk                                   */
/*==============================================================*/
create index est_composee_de_fk on ods_critere
(
   id_requete
);

/*==============================================================*/
/* Index : valeur_pour_fk                                       */
/*==============================================================*/
create index valeur_pour_fk on ods_critere
(
   id_type_critere
);

/*==============================================================*/
/* Table : ods_direction                                        */
/*==============================================================*/
create table ods_direction
(
   id_direction                   int(8)                   not null,
   code_direction                 varchar(6)                        not null,
   libelle_court                  text                           not null,
   libelle_long                   text                           not null,
   actif                          bool,
   primary key (id_direction)
)
comment = "Contient la liste des directions"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_elmt_releve_travaux                              */
/*==============================================================*/
create table ods_elmt_releve_travaux
(
   id_elmt_releve                 int(8)                   not null,
   id_pdd                         int(8),
   id_releve                      int(8)                   not null,
   id_va                          int(8),
   id_elu                         int(8),
   id_groupe                      int(8),
   pour                           int(8),
   contre                         int(8),
   abst                           int(8),
   nppv                           int(8),
   observations                   text,
   primary key (id_elmt_releve)
)
comment = "Contient les éléments du relevé des travaux d'une commission"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : vote_ou_obs_pour_fk                                  */
/*==============================================================*/
create index vote_ou_obs_pour_fk on ods_elmt_releve_travaux
(
   id_va
);

/*==============================================================*/
/* Index : emise_par_elu_fk                                     */
/*==============================================================*/
create index emise_par_elu_fk on ods_elmt_releve_travaux
(
   id_elu
);

/*==============================================================*/
/* Index : emise_par_fk                                         */
/*==============================================================*/
create index emise_par_fk on ods_elmt_releve_travaux
(
   id_groupe
);

/*==============================================================*/
/* Index : observation_pour_fk                                  */
/*==============================================================*/
create index observation_pour_fk on ods_elmt_releve_travaux
(
   id_pdd
);

/*==============================================================*/
/* Index : contient_les_elmts_fk                                */
/*==============================================================*/
create index contient_les_elmts_fk on ods_elmt_releve_travaux
(
   id_releve
);

/*==============================================================*/
/* Table : ods_elu                                              */
/*==============================================================*/
create table ods_elu
(
   id_elu                         int(8)                   not null,
   id_groupe                      int(8)                   not null,
   id_commission                  int(8),
   ods_id_elu                     int(8),
   civilite                       varchar(4)                        not null,
   nom_elu                        varchar(50)                    not null,
   prenom_elu                     varchar(30)                    not null,
   actif                          bool,
   primary key (id_elu)
)
comment = "Contient les élus"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : rapporte_dans_fk                                     */
/*==============================================================*/
create index rapporte_dans_fk on ods_elu
(
   id_commission
);

/*==============================================================*/
/* Index : est_remplace_par_fk                                  */
/*==============================================================*/
create index est_remplace_par_fk on ods_elu
(
   ods_id_elu
);

/*==============================================================*/
/* Index : appartient_a_fk                                      */
/*==============================================================*/
create index appartient_a_fk on ods_elu
(
   id_groupe
);

/*==============================================================*/
/* Table : ods_entree_commission                                */
/*==============================================================*/
create table ods_entree_commission
(
   id_entree                      int(8)                   not null,
   id_commission                  int(8)                   not null,
   primary key (id_entree, id_commission)
)
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : ods_entree_commission_fk                             */
/*==============================================================*/
create index ods_entree_commission_fk on ods_entree_commission
(
   id_entree
);

/*==============================================================*/
/* Index : ods_entree_commission2_fk                            */
/*==============================================================*/
create index ods_entree_commission2_fk on ods_entree_commission
(
   id_commission
);

/*==============================================================*/
/* Table : ods_entree_elus                                      */
/*==============================================================*/
create table ods_entree_elus
(
   id_elu                         int(8)                   not null,
   id_entree                      int(8)                   not null,
   primary key (id_elu, id_entree)
)
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : ods_entree_elus_fk                                   */
/*==============================================================*/
create index ods_entree_elus_fk on ods_entree_elus
(
   id_elu
);

/*==============================================================*/
/* Index : ods_entree_elus2_fk                                  */
/*==============================================================*/
create index ods_entree_elus2_fk on ods_entree_elus
(
   id_entree
);

/*==============================================================*/
/* Table : ods_entree_ordre_jour                                */
/*==============================================================*/
create table ods_entree_ordre_jour
(
   id_entree                      int(8)                   not null,
   id_va                          int(8),
   id_odj                         int(8)                   not null,
   id_nature                      int(8),
   id_pdd                         int(8),
   type_entree                    varchar(1),
   numero_ordre                   int(8)                   not null,
   texte_libre                    text,
   reference_affichee             varchar(60),
   objet                          text,
   style                          varchar(20),
   primary key (id_entree)
)
comment = "Contient les entrées de l'ordre du jour"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : est_un_pdd_fk                                        */
/*==============================================================*/
create index est_un_pdd_fk on ods_entree_ordre_jour
(
   id_pdd
);

/*==============================================================*/
/* Index : est_classe_fk                                        */
/*==============================================================*/
create index est_classe_fk on ods_entree_ordre_jour
(
   id_nature
);

/*==============================================================*/
/* Index : contient_fk                                          */
/*==============================================================*/
create index contient_fk on ods_entree_ordre_jour
(
   id_odj
);

/*==============================================================*/
/* Index : est_un_voeu_fk                                       */
/*==============================================================*/
create index est_un_voeu_fk on ods_entree_ordre_jour
(
   id_va
);

/*==============================================================*/
/* Table : ods_expression_usuelle                               */
/*==============================================================*/
create table ods_expression_usuelle
(
   id_expression                  int(8)                   not null,
   expression                     text                           not null,
   primary key (id_expression)
)
comment = "Contient les expressions usuelles"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_fascicule                                        */
/*==============================================================*/
create table ods_fascicule
(
   id_fascicule                   int(8)                   not null,
   id_seance                      int(8)                   not null,
   code_fascicule                 varchar(1),
   nom_fascicule                  varchar(50)                       not null,
   numero_ordre                   int(8),
   primary key (id_fascicule)
)
comment = "Contient les fascicules des liasses de voeux et amendements"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : liasse_de_la_seance_fk                               */
/*==============================================================*/
create index liasse_de_la_seance_fk on ods_fascicule
(
   id_seance
);

/*==============================================================*/
/* Table : ods_fichier                                          */
/*==============================================================*/
create table ods_fichier
(
   id_document                    int(8)                   not null,
   id_pdd                         int(8),
   id_commission                  int(8),
   id_formation_conseil           int(8),
   id_type_document               int(8)                   not null,
   id_fichier                     int(8)                   not null,
   id_seance                      int(8),
   intitule                       varchar(100),
   texte                          text,
   extension                      varchar(5),
   taille                         int(8),
   nom                            text,
   en_ligne                       bool                           not null,
   date_publication               timestamp,
   version                        int(8),
   primary key (id_document)
)
comment = "Contient les fichiers d'une séance du conseil, d'un PDD ou d'une commission"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : pour_le_pdd_fk                                       */
/*==============================================================*/
create index pour_le_pdd_fk on ods_fichier
(
   id_pdd
);

/*==============================================================*/
/* Index : est_de_type_fk                                       */
/*==============================================================*/
create index est_de_type_fk on ods_fichier
(
   id_type_document
);

/*==============================================================*/
/* Index : pour_la_seance_fk                                    */
/*==============================================================*/
create index pour_la_seance_fk on ods_fichier
(
   id_seance
);

/*==============================================================*/
/* Index : pour_la_commission_fk                                */
/*==============================================================*/
create index pour_la_commission_fk on ods_fichier
(
   id_commission
);

/*==============================================================*/
/* Index : stocke_fk                                            */
/*==============================================================*/
create index stocke_fk on ods_fichier
(
   id_fichier
);

/*==============================================================*/
/* Index : liasse_des_va_de_fk                                  */
/*==============================================================*/
create index liasse_des_va_de_fk on ods_fichier
(
   id_formation_conseil
);

/*==============================================================*/
/* Table : ods_fichier_physique                                 */
/*==============================================================*/
create table ods_fichier_physique
(
   id_fichier                     int(8)                   not null,
   fichier                        longblob                       not null,
   primary key (id_fichier)
)
comment = "Permet de stocker les fichiers physiques"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_formation_conseil                                */
/*==============================================================*/
create table ods_formation_conseil
(
   id_formation_conseil           int(8)                   not null,
   libelle_formation_conseil      varchar(50)                    not null,
   primary key (id_formation_conseil)
)
comment = "M - Municipal
G - Général"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_groupe                                           */
/*==============================================================*/
create table ods_groupe
(
   id_groupe                      int(8)                   not null,
   nom_groupe                     varchar(50)                    not null,
   nom_complet                    varchar(100)                   not null,
   actif                          bool,
   primary key (id_groupe)
)
comment = "Contient les groupes politiques"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_historique                                       */
/*==============================================================*/
create table ods_historique
(
   id_historique                  int(8)                   not null,
   id_document                    int(8),
   id_pdd                         int(8),
   id_va                          int(8),
   version                        int(8)                   not null,
   date_publication               timestamp                      not null,
   primary key (id_historique)
)
comment = "Contient les historiques des versions"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : pdd_mis_a_jour_fk                                    */
/*==============================================================*/
create index pdd_mis_a_jour_fk on ods_historique
(
   id_pdd
);

/*==============================================================*/
/* Index : va_mis_a_jour_fk                                     */
/*==============================================================*/
create index va_mis_a_jour_fk on ods_historique
(
   id_va
);

/*==============================================================*/
/* Index : fichier_mis_a_jour_fk                                */
/*==============================================================*/
create index fichier_mis_a_jour_fk on ods_historique
(
   id_document
);

/*==============================================================*/
/* Table : ods_modele_ordre_jour                                */
/*==============================================================*/
create table ods_modele_ordre_jour
(
   id_modele                      int(8)                   not null,
   id_fichier                     int(8),
   id_commission                  int(8),
   id_type_odj                    int(8)                   not null,
   ods_id_fichier                 int(8),
   id_formation_conseil           int(8)                   not null,
   titre                          text                           not null,
   primary key (id_modele)
)
comment = "Contient les modèles d'ordre du jour"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : du_conseil_fk                                        */
/*==============================================================*/
create index du_conseil_fk on ods_modele_ordre_jour
(
   id_formation_conseil
);

/*==============================================================*/
/* Index : pour_odj_de_type_fk                                  */
/*==============================================================*/
create index pour_odj_de_type_fk on ods_modele_ordre_jour
(
   id_type_odj
);

/*==============================================================*/
/* Index : entete_de_odj_fk                                     */
/*==============================================================*/
create index entete_de_odj_fk on ods_modele_ordre_jour
(
   ods_id_fichier
);

/*==============================================================*/
/* Index : pied_de_odj_fk                                       */
/*==============================================================*/
create index pied_de_odj_fk on ods_modele_ordre_jour
(
   id_fichier
);

/*==============================================================*/
/* Index : modele_de_la_commission_fk                           */
/*==============================================================*/
create index modele_de_la_commission_fk on ods_modele_ordre_jour
(
   id_commission
);

/*==============================================================*/
/* Table : ods_nature_dossier                                   */
/*==============================================================*/
create table ods_nature_dossier
(
   id_nature                      int(8)                   not null,
   id_seance                      int(8)                   not null,
   num_nature                     int(8)                   not null,
   libelle_nature                 text                           not null,
   primary key (id_nature)
)
comment = "Contient les natures de dossiers"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : definie_pour_fk                                      */
/*==============================================================*/
create index definie_pour_fk on ods_nature_dossier
(
   id_seance
);

/*==============================================================*/
/* Table : ods_odj                                              */
/*==============================================================*/
create table ods_odj
(
   id_odj                         int(8)                   not null,
   id_seance                      int(8)                   not null,
   id_formation_conseil           int(8)                   not null,
   id_type_odj                    int(8)                   not null,
   ods_id_odj                     int(8),
   id_commission                  int(8),
   intitule                       varchar(100)                   not null,
   mode_classement                varchar(1)                        not null,
   tourniquet                     bool                           not null,
   publie                         bool                           not null,
   date_publication               timestamp,
   est_sauvegarde                 bool                           not null,
   xml_correspondant              text,
   xml_entete              		  text,
   xml_pied_de_page				  text,
   primary key (id_odj)
)
comment = "Contient les ordres du jour d'une formation de conseil"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : de_la_seance_fk                                      */
/*==============================================================*/
create index de_la_seance_fk on ods_odj
(
   id_seance
);

/*==============================================================*/
/* Index : de_la_formation_fk                                   */
/*==============================================================*/
create index de_la_formation_fk on ods_odj
(
   id_formation_conseil
);

/*==============================================================*/
/* Index : de_la_commission_fk                                  */
/*==============================================================*/
create index de_la_commission_fk on ods_odj
(
   id_commission
);

/*==============================================================*/
/* Index : type_de_fk                                           */
/*==============================================================*/
create index type_de_fk on ods_odj
(
   id_type_odj
);

/*==============================================================*/
/* Index : est_une_sauvegarde_de_fk                             */
/*==============================================================*/
create index est_une_sauvegarde_de_fk on ods_odj
(
   ods_id_odj
);

/*==============================================================*/
/* Table : ods_panier                                           */
/*==============================================================*/
create table ods_panier
(
   id_element_panier              int(8)                   not null,
   id_document                    int(8)                   not null,
   id_utilisateur                 varchar(50)                   not null,
   date_ajout                     timestamp,
   est_zip                        tinyint(1)                    NOT NULL default '0',
   primary key (id_element_panier)
)
comment = "Contient les documents du panier de l''utilisateur"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : contient_le_fichier_fk                               */
/*==============================================================*/
create index contient_le_fichier_fk on ods_panier
(
   id_document
);

/*==============================================================*/
/* Index : dans_le_panier_de_fk                                 */
/*==============================================================*/
create index dans_le_panier_de_fk on ods_panier
(
   id_utilisateur
);

/*==============================================================*/
/* Table : ods_pdd                                              */
/*==============================================================*/
create table ods_pdd
(
   id_pdd                         int(8)                   not null,
   id_categorie                   int(8),
   id_direction                   int(8),
   id_formation_conseil           int(8)                   not null,
   id_statut                      int(8),
   id_groupe                      int(8),
   reference                      varchar(50),
   type_pdd                       varchar(2)                        not null,
   delegations_services           bool                           not null,
   mode_introduction              varchar(2)                        not null,
   objet                          text                           not null,
   pieces_manuelles               bool                           not null,
   date_vote                      timestamp,
   date_retour_ctrl_legalite      timestamp,
   en_ligne                       bool                           not null,
   date_publication               timestamp,
   version                        int(8),
   primary key (id_pdd)
)
comment = "Contient les projets et propositions de délibération"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : principale_fk                                        */
/*==============================================================*/
create index principale_fk on ods_pdd
(
   id_direction
);

/*==============================================================*/
/* Index : de_categorie_fk                                      */
/*==============================================================*/
create index de_categorie_fk on ods_pdd
(
   id_categorie
);

/*==============================================================*/
/* Index : pdd_pour_le_conseil_fk                               */
/*==============================================================*/
create index pdd_pour_le_conseil_fk on ods_pdd
(
   id_formation_conseil
);

/*==============================================================*/
/* Index : proposee_par_fk                                      */
/*==============================================================*/
create index proposee_par_fk on ods_pdd
(
   id_groupe
);

/*==============================================================*/
/* Index : pdd_a_ete_fk                                         */
/*==============================================================*/
create index pdd_a_ete_fk on ods_pdd
(
   id_statut
);

/*==============================================================*/
/* Table : ods_releve_travaux                                   */
/*==============================================================*/
create table ods_releve_travaux
(
   id_releve                      int(8)                   not null,
   id_commission                  int(8)                   not null,
   id_seance                      int(8)                   not null,
   intitule                       varchar(100)                   not null,
   en_ligne                       bool                           not null,
   primary key (id_releve)
)
comment = "Contient les relevés de travaux des commissions"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : releve_de_fk                                         */
/*==============================================================*/
create index releve_de_fk on ods_releve_travaux
(
   id_commission
);

/*==============================================================*/
/* Index : dans_fk                                              */
/*==============================================================*/
create index dans_fk on ods_releve_travaux
(
   id_seance
);

/*==============================================================*/
/* Table : ods_requete                                          */
/*==============================================================*/
create table ods_requete
(
  id_requete decimal(8,0) NOT NULL default '0',
  id_utilisateur varchar(50) NOT NULL default '0',
  intitule varchar(100) NOT NULL default '',
  type_requete varchar(10) NOT NULL default '',
  is_notifie tinyint(1) NOT NULL default '0',
  is_archive tinyint(1) NOT NULL default '0',
  date_creation timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (id_requete)
)
comment = "Contient les requêtes personnelles pré enregistrées de l''utilisateur"
type = InnoDB default charset=utf8;


/*==============================================================*/
/* Index : enregistree_par_fk                                   */
/*==============================================================*/
create index enregistree_par_fk on ods_requete
(
   id_utilisateur
);

/*==============================================================*/
/* Table : ods_seance                                           */
/*==============================================================*/
create table ods_seance
(
   id_seance                      int(8)                   not null,
   date_seance                    timestamp                      not null,
   date_fin_seance                timestamp                      not null,
   date_remise_cr_sommaire        timestamp                      not null,
   date_depot_questions           timestamp                      not null,
   date_limite_diff_questions     timestamp                      not null,
   date_limite_diff_pdd           timestamp                      not null,
   date_limite_diff_dsp           timestamp                      not null,
   date_conference                timestamp                      not null,
   date_reunion_post_commission   timestamp                      not null,
   date_reunion_premiere_commission timestamp                    not null,
   date_reunion_deuxieme_commission timestamp                    not null,
   date_reunion_troisieme_commission timestamp                   not null,
   date_publication               timestamp                      NULL,
   taille_archive                 int(10),
   primary key (id_seance)
)
comment = "Contient les dates des séances du conseil de Paris"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_statut                                           */
/*==============================================================*/
create table ods_statut
(
   id_statut                      int(8)                   not null,
   libelle_statut                 varchar(50)                    not null,
   est_pour_pdd                   bool,
   est_pour_voeu                  bool,
   est_pour_amendement            bool,
   primary key (id_statut)
)
comment = "Contient les statuts d'un PDD, voeu ou amendement :
1 - Adopté
2 - Retiré
3 - Rejeté
4 - Amendé
5 - Non présenté
6 - Voeu transformé en amendement adopté
7 - Amendement transformé en voeu adopté
8 - Pas de vote"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_tourniquet                                       */
/*==============================================================*/
create table ods_tourniquet
(
   id_commission                  int(8)                   not null,
   num_ordre                      int(8)                   not null,
   primary key (id_commission)
)
comment = "Contient le tourniquet"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_type_de_critere                                  */
/*==============================================================*/
create table ods_type_de_critere
(
   id_type_critere                int(8)                   not null,
   nom_critere                    varchar(50)                    not null,
   primary key (id_type_critere)
)
comment = "Contient les critères de sélection disponibles à la recherche :
Mots clés
Code affaire du PDD
Type de documents recherchés
Formation du conseil
Commission
Rapporteur
Arrondissements
Directions
Catégories de délibération
Groupes
Elus"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_type_document                                    */
/*==============================================================*/
create table ods_type_document
(
   id_type_document               int(8)                   not null,
   libelle_type                   text                           not null,
   est_creation_prochaine_seance  bool,
   est_creation_aval              bool,
   primary key (id_type_document)
)
comment = "Contient les types de documents :
1 - Exposé des motifs
2 - Projet de délibéré
3 - Piece annexe
4 - Ordre du jour
5 - Convocation du Maire
6 - Liste intégrale des questions
7 - Relevé de la Conférence d''organisation
8 - Résumé des questions
9 - Document libre
10 - Compte-rendu
11 - Désignation
12 - Délibération
13 - Délibération de désignation
14 - Voeu
15 - Amendement
16 - Relevé
17 - Liasse des VA

"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_type_ordre_jour                                  */
/*==============================================================*/
create table ods_type_ordre_jour
(
   id_type_odj                    int(8)                   not null,
   libelle_type_odj               varchar(30)                    not null,
   libelle_long_type_odj           varchar(100),
   primary key (id_type_odj)
)
comment = "Contient les types d''ordre du jour :
1 - Prévisionnel
2 - Mis à jour
3 - Définitif
4 - Commission
5 - Additif
6 - Rectificatif"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_utilisateur                                      */
/*==============================================================*/
create table ods_utilisateur
(
   id_utilisateur                 varchar(50)                   not null,
   nom_utilisateur                varchar(50)                   not null,
   prenom_utilisateur             varchar(30)                   not null,
   email_utilisateur              varchar(100)                  not null,
   email_1_copie                  varchar(100),
   email_2_copie                  varchar(100),
   derniere_connexion             timestamp,
   dernier_id_session             varchar(50)                   not null	default '0',
   primary key (id_utilisateur)
)
comment = "Contient les utilisateurs"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Table : ods_va_depose_par                                    */
/*==============================================================*/
create table ods_va_depose_par
(
   id_elu                         int(8)                   not null,
   id_va                          int(8)                   not null,
   primary key (id_elu, id_va)
)
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : ods_va_depose_par_fk                                 */
/*==============================================================*/
create index ods_va_depose_par_fk on ods_va_depose_par
(
   id_elu
);

/*==============================================================*/
/* Index : ods_va_depose_par2_fk                                */
/*==============================================================*/
create index ods_va_depose_par2_fk on ods_va_depose_par
(
   id_va
);

/*==============================================================*/
/* Table : ods_va_rattache_pdd                                  */
/*==============================================================*/
create table ods_va_rattache_pdd
(
   id_va                          int(8)                   not null,
   id_pdd                         int(8)                   not null,
   numero_ordre                   int(8),
   primary key (id_va, id_pdd)
)
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : ods_va_rattache_pdd_fk                               */
/*==============================================================*/
create index ods_va_rattache_pdd_fk on ods_va_rattache_pdd
(
   id_va
);

/*==============================================================*/
/* Index : ods_va_rattache_pdd2_fk                              */
/*==============================================================*/
create index ods_va_rattache_pdd2_fk on ods_va_rattache_pdd
(
   id_pdd
);

/*==============================================================*/
/* Table : ods_voeu_amendement                                  */
/*==============================================================*/
create table ods_voeu_amendement
(
   id_va                          int(8)                   not null,
   id_delib                       int(8),
   id_formation_conseil           int(8)                   not null,
   id_commission                  int(8),
   ods_id_va                      int(8),
   id_texte_initial               int(8)                   not null,
   id_statut                      int(8),
   id_fascicule                   int(8)                   not null,
   type                           varchar(3)                        not null,
   reference_va                   varchar(10),
   objet                          text                           not null,
   depose_executif                bool                           not null,
   num_deliberation               varchar(10),
   date_vote                      timestamp                      NULL,
   date_retour_ctrl_legalite      timestamp                      NULL,
   en_ligne                       bool                           not null,
   date_publication               timestamp                      NULL,
   version                        int(8),
   primary key (id_va)
)
comment = "Contient les voeux et amendements"
type = InnoDB default charset=utf8;


create table  ods_front_indexer (
  id_indexer decimal(8,0) not null default '0',
  nom_indexer varchar(100) not null,
  init_index tinyint(1) not null default '0',
  primary key  (id_indexer)
)
comment="liste des serveurs d''indexation  pouvant écrire sur l''index"
type = InnoDB default charset=utf8;

create table  ods_index_task (
  id_task decimal(8,0) NOT NULL default '0',
  type_objet decimal(1,0) NOT NULL default '0',
  id_objet decimal(8,0) NOT NULL default '0',
  id_indexer decimal(8,0) NOT NULL default '0',
  code_task decimal(8,0) NOT NULL default '0',
  is_archive tinyint(1) NOT NULL default '0',
  primary key  (id_task)
)
comment="Actions à réaliser sur l''index Lucene"
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : voeu_pour_le_conseil_fk                              */
/*==============================================================*/
create index voeu_pour_le_conseil_fk on ods_voeu_amendement
(
   id_formation_conseil
);

/*==============================================================*/
/* Index : appartient_au_fk                                     */
/*==============================================================*/
create index appartient_au_fk on ods_voeu_amendement
(
   id_fascicule
);

/*==============================================================*/
/* Index : bis_ou_ter_de_fk                                     */
/*==============================================================*/
create index bis_ou_ter_de_fk on ods_voeu_amendement
(
   ods_id_va
);

/*==============================================================*/
/* Index : texte_initial_va_fk                                  */
/*==============================================================*/
create index texte_initial_va_fk on ods_voeu_amendement
(
   id_texte_initial
);

/*==============================================================*/
/* Index : deliberation_visee_fk                                */
/*==============================================================*/
create index deliberation_visee_fk on ods_voeu_amendement
(
   id_delib
);

/*==============================================================*/
/* Index : va_a_ete_fk                                          */
/*==============================================================*/
create index va_a_ete_fk on ods_voeu_amendement
(
   id_statut
);

/*==============================================================*/
/* Index : depose_par_fk                                        */
/*==============================================================*/
create index depose_par_fk on ods_voeu_amendement
(
   id_commission
);

/*==============================================================*/
/* Table : ods_publication_parisfr                                      */
/*==============================================================*/
create table ods_publication_parisfr
(
   id_publication                         int(8)                   not null,
   id_odj                   			  int(8)                   not null,
   primary key (id_publication)
)
type = InnoDB default charset=utf8;

/*==============================================================*/
/* Index : contient odj                                */
/*==============================================================*/
create index contient_odj_fk on  ods_publication_parisfr
(
   id_odj
);


alter table ods_arrondissement_du_pdd add constraint FK_LOCALISE_DANS foreign key (id_pdd)
      references ods_pdd (id_pdd) on delete restrict on update restrict;

alter table ods_co_emettrice add constraint FK_ODS_CO_EMETTRICE foreign key (id_pdd)
      references ods_pdd (id_pdd) on delete restrict on update restrict;

alter table ods_co_emettrice add constraint FK_ODS_CO_EMETTRICE2 foreign key (id_direction)
      references ods_direction (id_direction) on delete restrict on update restrict;

alter table ods_critere add constraint FK_EST_COMPOSEE_DE foreign key (id_requete)
      references ods_requete (id_requete) on delete restrict on update restrict;

alter table ods_critere add constraint FK_VALEUR_POUR foreign key (id_type_critere)
      references ods_type_de_critere (id_type_critere) on delete restrict on update restrict;

alter table ods_elmt_releve_travaux add constraint FK_CONTIENT_LES_ELMTS foreign key (id_releve)
      references ods_releve_travaux (id_releve) on delete restrict on update restrict;

alter table ods_elmt_releve_travaux add constraint FK_EMISE_PAR foreign key (id_groupe)
      references ods_groupe (id_groupe) on delete restrict on update restrict;

alter table ods_elmt_releve_travaux add constraint FK_EMISE_PAR_ELU foreign key (id_elu)
      references ods_elu (id_elu) on delete restrict on update restrict;

alter table ods_elmt_releve_travaux add constraint FK_OBSERVATION_POUR foreign key (id_pdd)
      references ods_pdd (id_pdd) on delete restrict on update restrict;

alter table ods_elmt_releve_travaux add constraint FK_VOTE_OU_OBS_POUR foreign key (id_va)
      references ods_voeu_amendement (id_va) on delete restrict on update restrict;

alter table ods_elu add constraint FK_APPARTIENT_A foreign key (id_groupe)
      references ods_groupe (id_groupe) on delete restrict on update restrict;

alter table ods_elu add constraint FK_EST_REMPLACE_PAR foreign key (ods_id_elu)
      references ods_elu (id_elu) on delete restrict on update restrict;

alter table ods_elu add constraint FK_RAPPORTE_DANS foreign key (id_commission)
      references ods_commission (id_commission) on delete restrict on update restrict;

alter table ods_entree_commission add constraint FK_ODS_ENTREE_COMMISSION foreign key (id_entree)
      references ods_entree_ordre_jour (id_entree) on delete restrict on update restrict;

alter table ods_entree_commission add constraint FK_ODS_ENTREE_COMMISSION2 foreign key (id_commission)
      references ods_commission (id_commission) on delete restrict on update restrict;

alter table ods_entree_elus add constraint FK_ODS_ENTREE_ELUS foreign key (id_elu)
      references ods_elu (id_elu) on delete restrict on update restrict;

alter table ods_entree_elus add constraint FK_ODS_ENTREE_ELUS2 foreign key (id_entree)
      references ods_entree_ordre_jour (id_entree) on delete restrict on update restrict;

alter table ods_entree_ordre_jour add constraint FK_CONTIENT foreign key (id_odj)
      references ods_odj (id_odj) on delete restrict on update restrict;

alter table ods_entree_ordre_jour add constraint FK_EST_CLASSE foreign key (id_nature)
      references ods_nature_dossier (id_nature) on delete restrict on update restrict;

alter table ods_entree_ordre_jour add constraint FK_EST_UN_PDD foreign key (id_pdd)
      references ods_pdd (id_pdd) on delete restrict on update restrict;

alter table ods_entree_ordre_jour add constraint FK_EST_UN_VOEU foreign key (id_va)
      references ods_voeu_amendement (id_va) on delete restrict on update restrict;

alter table ods_fascicule add constraint FK_LIASSE_DE_LA_SEANCE foreign key (id_seance)
      references ods_seance (id_seance) on delete restrict on update restrict;

alter table ods_fichier add constraint FK_EST_DE_TYPE foreign key (id_type_document)
      references ods_type_document (id_type_document) on delete restrict on update restrict;

alter table ods_fichier add constraint FK_LIASSE_DES_VA_DE foreign key (id_formation_conseil)
      references ods_formation_conseil (id_formation_conseil) on delete restrict on update restrict;

alter table ods_fichier add constraint FK_POUR_LA_COMMISSION foreign key (id_commission)
      references ods_commission (id_commission) on delete restrict on update restrict;

alter table ods_fichier add constraint FK_POUR_LA_SEANCE foreign key (id_seance)
      references ods_seance (id_seance) on delete restrict on update restrict;

alter table ods_fichier add constraint FK_POUR_LE_PDD foreign key (id_pdd)
      references ods_pdd (id_pdd) on delete restrict on update restrict;

alter table ods_fichier add constraint FK_STOCKE foreign key (id_fichier)
      references ods_fichier_physique (id_fichier) on delete restrict on update restrict;

alter table ods_historique add constraint FK_FICHIER_MIS_A_JOUR foreign key (id_document)
      references ods_fichier (id_document) on delete restrict on update restrict;

alter table ods_historique add constraint FK_PDD_MIS_A_JOUR foreign key (id_pdd)
      references ods_pdd (id_pdd) on delete restrict on update restrict;

alter table ods_historique add constraint FK_VA_MIS_A_JOUR foreign key (id_va)
      references ods_voeu_amendement (id_va) on delete restrict on update restrict;

alter table ods_modele_ordre_jour add constraint FK_DU_CONSEIL foreign key (id_formation_conseil)
      references ods_formation_conseil (id_formation_conseil) on delete restrict on update restrict;

alter table ods_modele_ordre_jour add constraint FK_ENTETE_DE_ODJ foreign key (ods_id_fichier)
      references ods_fichier_physique (id_fichier) on delete restrict on update restrict;

alter table ods_modele_ordre_jour add constraint FK_MODELE_DE_LA_COMMISSION foreign key (id_commission)
      references ods_commission (id_commission) on delete restrict on update restrict;

alter table ods_modele_ordre_jour add constraint FK_PIED_DE_ODJ foreign key (id_fichier)
      references ods_fichier_physique (id_fichier) on delete restrict on update restrict;

alter table ods_modele_ordre_jour add constraint FK_POUR_ODJ_DE_TYPE foreign key (id_type_odj)
      references ods_type_ordre_jour (id_type_odj) on delete restrict on update restrict;

alter table ods_nature_dossier add constraint FK_DEFINIE_POUR foreign key (id_seance)
      references ods_seance (id_seance) on delete restrict on update restrict;

alter table ods_odj add constraint FK_DE_LA_COMMISSION foreign key (id_commission)
      references ods_commission (id_commission) on delete restrict on update restrict;

alter table ods_odj add constraint FK_DE_LA_FORMATION foreign key (id_formation_conseil)
      references ods_formation_conseil (id_formation_conseil) on delete restrict on update restrict;

alter table ods_odj add constraint FK_DE_LA_SEANCE foreign key (id_seance)
      references ods_seance (id_seance) on delete restrict on update restrict;

alter table ods_odj add constraint FK_EST_UNE_SAUVEGARDE_DE foreign key (ods_id_odj)
      references ods_odj (id_odj) on delete restrict on update restrict;

alter table ods_odj add constraint FK_TYPE_DE foreign key (id_type_odj)
      references ods_type_ordre_jour (id_type_odj) on delete restrict on update restrict;

alter table ods_panier add constraint FK_CONTIENT_LE_FICHIER foreign key (id_document)
      references ods_fichier (id_document) on delete restrict on update restrict;

alter table ods_panier add constraint FK_DANS_LE_PANIER_DE foreign key (id_utilisateur)
      references ods_utilisateur (id_utilisateur) on delete restrict on update restrict;

alter table ods_pdd add constraint FK_DE_CATEGORIE foreign key (id_categorie)
      references ods_categorie_deliberation (id_categorie) on delete restrict on update restrict;

alter table ods_pdd add constraint FK_PDD_A_ETE foreign key (id_statut)
      references ods_statut (id_statut) on delete restrict on update restrict;

alter table ods_pdd add constraint FK_PDD_POUR_LE_CONSEIL foreign key (id_formation_conseil)
      references ods_formation_conseil (id_formation_conseil) on delete restrict on update restrict;

alter table ods_pdd add constraint FK_PRINCIPALE foreign key (id_direction)
      references ods_direction (id_direction) on delete restrict on update restrict;

alter table ods_pdd add constraint FK_PROPOSEE_PAR foreign key (id_groupe)
      references ods_groupe (id_groupe) on delete restrict on update restrict;

alter table ods_releve_travaux add constraint FK_DANS foreign key (id_seance)
      references ods_seance (id_seance) on delete restrict on update restrict;

alter table ods_releve_travaux add constraint FK_RELEVE_DE foreign key (id_commission)
      references ods_commission (id_commission) on delete restrict on update restrict;

alter table ods_requete add constraint FK_ENREGISTREE_PAR foreign key (id_utilisateur)
      references ods_utilisateur (id_utilisateur) on delete restrict on update restrict;

alter table ods_tourniquet add constraint FK_ORDONNEE foreign key (id_commission)
      references ods_commission (id_commission) on delete restrict on update restrict;

alter table ods_va_depose_par add constraint FK_ODS_VA_DEPOSE_PAR foreign key (id_elu)
      references ods_elu (id_elu) on delete restrict on update restrict;

alter table ods_va_depose_par add constraint FK_ODS_VA_DEPOSE_PAR2 foreign key (id_va)
      references ods_voeu_amendement (id_va) on delete restrict on update restrict;

alter table ods_va_rattache_pdd add constraint FK_ODS_VA_RATTACHE_PDD foreign key (id_va)
      references ods_voeu_amendement (id_va) on delete restrict on update restrict;

alter table ods_va_rattache_pdd add constraint FK_ODS_VA_RATTACHE_PDD2 foreign key (id_pdd)
      references ods_pdd (id_pdd) on delete restrict on update restrict;

alter table ods_voeu_amendement add constraint FK_APPARTIENT_AU foreign key (id_fascicule)
      references ods_fascicule (id_fascicule) on delete restrict on update restrict;

alter table ods_voeu_amendement add constraint FK_BIS_OU_TER_DE foreign key (ods_id_va)
      references ods_voeu_amendement (id_va) on delete restrict on update restrict;

alter table ods_voeu_amendement add constraint FK_DELIBERATION foreign key (id_delib)
      references ods_fichier (id_document) on delete restrict on update restrict;

alter table ods_voeu_amendement add constraint FK_DEPOSE_PAR foreign key (id_commission)
      references ods_commission (id_commission) on delete restrict on update restrict;

alter table ods_voeu_amendement add constraint FK_TEXTE_INITIAL_VA foreign key (id_texte_initial)
      references ods_fichier (id_document) on delete restrict on update restrict;

alter table ods_voeu_amendement add constraint FK_VA_A_ETE foreign key (id_statut)
      references ods_statut (id_statut) on delete restrict on update restrict;

alter table ods_voeu_amendement add constraint FK_VOEU_POUR_LE_CONSEIL foreign key (id_formation_conseil)
      references ods_formation_conseil (id_formation_conseil) on delete restrict on update restrict;

alter table ods_index_task add constraint FK_SUR_INDEXER foreign key (id_indexer) references ods_front_indexer (id_indexer) on delete restrict on update restrict;
alter table ods_publication_parisfr add constraint FK_ODJ_PUBLIE foreign key (id_odj)
      references ods_odj (id_odj) on delete restrict on update restrict;
