-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[ShowGenre] (
    [Show]  UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_ShowGenre_Show  FOREIGN KEY REFERENCES [dbo].[Show]([Id]),
    [Genre] UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_ShowGenre_Genre FOREIGN KEY REFERENCES [dbo].[Genre]([Id]),
    CONSTRAINT PK_ShowGenre PRIMARY KEY ([Show], [Genre])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[ShowGenre];
-- +goose StatementEnd
