-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Like] (
    [Account] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [Episode] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Episode]([Id]),
    [CreatedTime] DATETIME,
    PRIMARY KEY ([Account], [Episode])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Like];
-- +goose StatementEnd
