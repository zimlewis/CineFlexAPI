-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Advertisement] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Link] VARCHAR(100) NOT NULL,
    [Image] VARCHAR(100) NOT NULL,
    [Enabled] BIT NOT NULL DEFAULT 1,
    [Type] INT NOT NULL,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [Hirer] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Hirer]([Id]) 
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Advertisement];
-- +goose StatementEnd
