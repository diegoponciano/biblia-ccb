--alter table palavra drop cabecalho;
--alter table palavra add column cabecalho varchar(1000);

select * from palavra where id_livro = 19 order by capitulo, versiculo;
