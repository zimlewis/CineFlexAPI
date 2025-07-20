-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Favorite] (
    [Show]        UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Favorite_Show    FOREIGN KEY REFERENCES [dbo].[Show]([Id]),
    [Account]     UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Favorite_Account FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [CreatedTime] DATETIME,
    CONSTRAINT PK_Favorite PRIMARY KEY ([Show], [Account])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Favorite];
-- +goose StatementEnd
