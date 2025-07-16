-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Like] (
    [Account]     UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Like_Account  FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [Episode]     UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Like_Episode  FOREIGN KEY REFERENCES [dbo].[Episode]([Id]),
    [CreatedTime] DATETIME,
    CONSTRAINT PK_Like PRIMARY KEY ([Account], [Episode])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Like];
-- +goose StatementEnd
